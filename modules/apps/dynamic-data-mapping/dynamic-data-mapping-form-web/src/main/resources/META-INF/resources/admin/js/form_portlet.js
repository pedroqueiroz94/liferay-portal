AUI.add(
	'liferay-ddm-form-portlet',
	function(A) {
		var LayoutSerializer = Liferay.DDM.LayoutSerializer;

		var EMPTY_FN = A.Lang.emptyFn;

		var MINUTE = 60000;

		var STR_TRANSLATION_MANAGER = 'translationManager';

		var STR_UNTITLED_ELEMENT_SET = Liferay.Language.get('untitled-element-set');

		var STR_UNTITLED_FORM = Liferay.Language.get('untitled-form');

		var FormPortlet = A.Component.create(
			{
				ATTRS: {
					alert: {
					},

					defaultLanguageId: {
						value: themeDisplay.getDefaultLanguageId()
					},

					editForm: {
					},

					editingLanguageId: {
						value: themeDisplay.getDefaultLanguageId()
					},

					formBuilder: {
						value: {}
					},

					formInstanceId: {
						getter: '_getFormInstanceId',
						value: 0
					},

					localizedDescription: {
						value: {}
					},

					localizedName: {
						value: {}
					},

					published: {
						lazyAdd: false,
						setter: '_setPublished',
						value: false
					},

					ruleBuilder: {
					},

					rules: {
						value: []
					},

					showPublishAlert: {
						value: false
					},

					translationManager: {
					},

					view: {
						value: 'forms'
					}
				},

				AUGMENTS: [Liferay.PortletBase],

				EXTENDS: A.Base,

				NAME: 'liferay-ddm-form-portlet',

				prototype: {
					initializer: function() {
						var instance = this;

						var formBuilder = instance.get('formBuilder');

						formBuilder.set('defaultLanguageId', instance.get('defaultLanguageId'));
						formBuilder.set('editingLanguageId', instance.get('editingLanguageId'));

						instance.layoutVisitor = new LayoutSerializer(
							{
								builder: formBuilder,
								defaultLanguageId: instance.get('defaultLanguageId')
							}
						);

						if (!instance._eventHandlers) {
							instance._eventHandlers = [];
						}

						if (window.DDMRuleBuilder) {
							instance._onRuleBuilderLoaded();
						}
						else {
							instance._eventHandlers.push(
								Liferay.on('RuleBuilderLoaded', A.bind('_onRuleBuilderLoaded', instance))
							);
						}

						if (instance.get('showPublishAlert')) {
							if (instance.get('published')) {
								instance._showPublishAlert();
							}
							else {
								instance._showUnpublishAlert()
							}
						}
					},

					renderUI: function() {
						var instance = this;

						instance.one('#loader').remove();

						instance.one('.portlet-forms').removeClass('hide');

						instance.get('formBuilder').render(instance.one('#formBuilder'));

						instance.createEditor(instance.ns('descriptionEditor'));
						instance.createEditor(instance.ns('nameEditor'));

						if (instance._isFormView()) {
							instance.get('ruleBuilder').render(instance.one('#ruleBuilder'));
							instance.createCopyPublishFormURLPopover();
						}
					},

					bindUI: function() {
						var instance = this;

						var formBuilder = instance.get('formBuilder');

						var translationManager = instance.get(STR_TRANSLATION_MANAGER);

						translationManager.on('editingLocaleChange', instance._afterEditingLocaleChange.bind(instance));

						var descriptionEditor = CKEDITOR.instances[instance.ns('descriptionEditor')];

						descriptionEditor.on('change', A.bind('_onDescriptionEditorChange', instance));

						var nameEditor = CKEDITOR.instances[instance.ns('nameEditor')];

						nameEditor.on('change', A.bind('_onNameEditorChange', instance));

						instance._eventHandlers.push(
							formBuilder._layoutBuilder.after('layout-builder:moveEnd', A.bind(instance._afterFormBuilderLayoutBuilderMoveEnd, instance)),
							formBuilder._layoutBuilder.after('layout-builder:moveStart', A.bind(instance._afterFormBuilderLayoutBuilderMoveStart, instance)),
							instance.one('.back-url-link').on('click', A.bind('_onBack', instance)),
							instance.one('#save').on('click', A.bind('_onSaveButtonClick', instance)),
							Liferay.on('destroyPortlet', A.bind('_onDestroyPortlet', instance))
						);

						var ruleBuilder = instance.get('ruleBuilder');

						if (ruleBuilder) {
							instance._eventHandlers.push(
								ruleBuilder.on('*:saveRule', A.bind('_autosave', instance, true))
							);
						}

						if (instance._isFormView()) {
							instance.bindNavigationBar();

							instance._eventHandlers.push(
								instance.after('autosave', instance._afterAutosave),
								A.one(nameEditor.element.$).on('keydown', A.bind('handleEditorTitleKeydown', instance)),
								A.one(nameEditor.element.$).on('keyup', A.bind('handleEditorTitleCopyAndPaste', instance)),
								A.one(nameEditor.element.$).on('keypress', A.bind('handleEditorTitleCopyAndPaste', instance)),
								instance.one('#preview').on('click', A.bind('_onPreviewButtonClick', instance)),
								instance.one('#publish').on('click', A.bind('_onPublishButtonClick', instance)),
								instance.one('#publishIcon').on('click', A.bind('_onPublishIconClick', instance))
							);

							var autosaveInterval = Liferay.DDM.FormSettings.autosaveInterval;

							if (autosaveInterval > 0) {
								instance._intervalId = setInterval(A.bind('_autosave', instance, true), autosaveInterval * MINUTE);
							}
						}
					},

					bindNavigationBar: function() {
						var instance = this;

						var ACTIONS = {
							'showForm': A.bind('_onFormButtonClick', instance),
							'showRules': A.bind('_onRulesButtonClick', instance)
						};

						instance._currentTab = 'formBuilder';

						Liferay.componentReady('formsNavigationBar').then(
							function(navigationBar) {
								navigationBar.on(
									'itemClicked',
									function(event) {
										var itemData = event.data.item.data;

										if (itemData && itemData.action && ACTIONS[itemData.action]) {
											ACTIONS[itemData.action]();
										}

										var newItems = this.items;

										newItems.forEach(
											function (item) {
												if (item.data.action === itemData.action) {
													item.active = true;
												}
												else {
													item.active = false;
												}
											}
										);

										this.items = newItems;
									}
								);
							}
						);
					},

					destructor: function() {
						var instance = this;

						clearInterval(instance._intervalId);

						instance.get('formBuilder').destroy();

						if (instance._isFormView()) {
							instance.get('ruleBuilder').destroy();
							instance._copyPublishFormURLPopover.destroy();
						}

						instance.savedState = null;

						(new A.EventHandle(instance._eventHandlers)).detach();
					},

					checkEditorLimit: function(e, textLimit) {
						var instance = this;

						var charCode = (e.which) ? e.which : e.keyCode;

						if (instance.isNotAllowedKey(e, textLimit) && (charCode != 91)) {
							e.preventDefault();
						}
					},

					createCopyPublishFormURLPopover: function() {
						var instance = this;

						instance._copyPublishFormURLPopover = new Liferay.DDM.FormBuilderCopyPublishFormURLPopover(
							{
								portletNamespace: instance.get('namespace')
							}
						);

						instance._copyPublishFormURLPopover.setAlign(
							{
								node: A.one('.publish-icon'),
								points: [A.WidgetPositionAlign.RC, A.WidgetPositionAlign.LC]
							}
						);
					},

					createEditor: function(editorName) {
						var instance = this;

						var editor = window[editorName];

						if (editor) {
							editor.create();
						}
						else {
							Liferay.once(
								'editorAPIReady',
								function(event) {
									if (event.editorName === editorName) {
										event.editor.create();
									}
								}
							);
						}
					},

					disableDescriptionEditor: function() {
						var instance = this;

						var descriptionEditor = CKEDITOR.instances[instance.ns('descriptionEditor')];

						descriptionEditor.setReadOnly(true);
					},

					disableNameEditor: function() {
						var instance = this;

						var nameEditor = CKEDITOR.instances[instance.ns('nameEditor')];

						nameEditor.setReadOnly(true);
					},

					enableDescriptionEditor: function() {
						var instance = this;

						var descriptionEditor = CKEDITOR.instances[instance.ns('descriptionEditor')];

						descriptionEditor.setReadOnly(false);
					},

					enableNameEditor: function() {
						var instance = this;

						var nameEditor = CKEDITOR.instances[instance.ns('nameEditor')];

						nameEditor.setReadOnly(false);
					},

					getState: function() {
						var instance = this;

						var formBuilder = instance.get('formBuilder');
						var ruleBuilder = instance.get('ruleBuilder');

						var pageManager = formBuilder.get('pageManager');

						instance.layoutVisitor.set('pages', formBuilder.get('layouts'));

						var translationManager = instance.get(STR_TRANSLATION_MANAGER);

						var state = {
							availableLanguageIds: translationManager.get('availableLocales'),
							defaultLanguageId: formBuilder.get('defaultLanguageId'),
							description: instance.get('localizedDescription'),
							name: instance.get('localizedName'),
							pages: instance.layoutVisitor.getPages(),
							paginationMode: pageManager.get('mode')
						};

						if (instance._isFormView()) {
							state = A.merge(
								state,
								{
									rules: ruleBuilder.get('rules'),
									successPageSettings: pageManager.get('successPageSettings')
								}
							);
						}

						return state;
					},

					handleEditorTitleCopyAndPaste: function(e) {
						var instance = this;

						return instance.preventCopyAndPaste(e, 120);
					},

					handleEditorTitleKeydown: function(e) {
						var instance = this;

						return instance.checkEditorLimit(e, 120);
					},

					isEmpty: function() {
						var instance = this;

						var formBuilder = instance.get('formBuilder');

						var count = 0;

						formBuilder.eachFormBuilderField(
							function(field) {
								count++;
							}
						);

						return count === 0;
					},

					isNotAllowedKey: function(e, textLimit) {
						var instance = this;

						var charCode = (e.which) ? e.which : e.keyCode;

						if ((e.currentTarget.text().length >= textLimit) && (e.isModifyingKey(charCode)) && (!e.isKeyInSet(charCode, 'BACKSPACE', 'ESC', 'ENTER'))) {
							return true;
						}
					},

					openConfirmationModal: function(confirm, cancel) {
						var instance = this;

						var dialog = Liferay.Util.Window.getWindow(
							{
								dialog: {
									bodyContent: Liferay.Language.get('any-unsaved-changes-will-be-lost-are-you-sure-you-want-to-leave'),
									destroyOnHide: true,
									height: 200,
									resizable: false,
									toolbars: {
										footer: [
											{
												cssClass: 'btn-secondary',
												label: Liferay.Language.get('leave-page'),
												on: {
													click: function() {
														confirm.call(instance, dialog);
													}
												}
											},
											{
												cssClass: 'btn-primary',
												label: Liferay.Language.get('stay'),
												on: {
													click: function() {
														cancel.call(instance, dialog);
													}
												}
											}
										]
									},
									width: 600
								},
								id: 'leaveFormDialog',
								title: Liferay.Language.get('leave-form')
							}
						);

						return dialog;
					},

					preventCopyAndPaste: function(e, textLimit) {
						var instance = this;
						var node = e.currentTarget._node;

						if (instance.isNotAllowedKey(e, textLimit)) {
							e.currentTarget.text(e.currentTarget.text().substr(0, textLimit));

							var range = document.createRange();
							var sel = window.getSelection();

							range.setStart(node.childNodes[0], node.textContent.length);
							range.collapse(true);
							sel.removeAllRanges();
							sel.addRange(range);
						}
					},

					submitForm: function() {
						var instance = this;

						instance.syncInputValues();

						var editForm = instance.get('editForm');

						Liferay.Util.submitForm(editForm.formNode);
					},

					syncInputValues: function() {
						var instance = this;

						var state = instance.getState();

						instance.one('#description').val(JSON.stringify(state.description));
						instance.one('#name').val(JSON.stringify(state.name));

						instance.one('#serializedFormBuilderContext').val(JSON.stringify(state));

						if (instance._isFormView()) {
							var settingsDDMForm = Liferay.component('settingsDDMForm');

							var publishedField = settingsDDMForm.getField('published');

							publishedField.set('value', instance.get('published'));

							var settings = settingsDDMForm.get('context');

							var settingsInput = instance.one('#serializedSettingsContext');

							settingsInput.val(JSON.stringify(settings));
						}
					},

					_addFieldButton: function() {
						var instance = this;

						var ruleButton = A.one('.lfr-ddm-add-rule');

						var publishButton = A.one('.publish-icon');

						if (ruleButton) {
							ruleButton.replaceClass('lfr-ddm-add-rule', 'lfr-ddm-add-field');
						}

						if (publishButton) {
							publishButton.removeClass('hide');
						}
					},

					_addRuleButton: function() {
						var instance = this;

						var addButton = A.one('.lfr-ddm-add-field');

						var publishButton = A.one('.publish-icon');

						if (addButton) {
							addButton.replaceClass('lfr-ddm-add-field', 'lfr-ddm-add-rule');
						}

						if (publishButton) {
							publishButton.addClass('hide');
						}
					},

					_afterAutosave: function(event) {
						var instance = this;

						instance._updateAutosaveBar(event.saveAsDraft, event.modifiedDate);

						var ruleBuilder = A.one('.lfr-ddm-add-rule');

						if (!ruleBuilder) {
							A.one('.publish-icon').removeClass('hide');
						}
					},

					_afterEditingLocaleChange: function(event) {
						var instance = this;

						var editingLanguageId = event.newVal;

						var defaultLanguageId = instance.get('defaultLanguageId');

						if (editingLanguageId !== defaultLanguageId) {
							instance.one('.lfr-ddm-plus-button').addClass('hide');
						}
						else {
							instance.one('.lfr-ddm-plus-button').removeClass('hide');
						}

						var formBuilder = instance.get('formBuilder');

						instance.set('editingLanguageId', editingLanguageId);
						formBuilder.set('editingLanguageId', editingLanguageId);

						instance._syncName();
						instance._syncDescription();
					},

					_afterFormBuilderLayoutBuilderMoveEnd: function() {
						var instance = this;

						instance.enableDescriptionEditor();
						instance.enableNameEditor();
					},

					_afterFormBuilderLayoutBuilderMoveStart: function() {
						var instance = this;

						instance.disableDescriptionEditor();
						instance.disableNameEditor();
					},

					_autosave: function(saveAsDraft, callback) {
						var instance = this;

						callback = callback || EMPTY_FN;

						instance.syncInputValues();

						var state = instance.getState();

						if (!instance.isEmpty()) {
							if (!instance._isSameState(instance.savedState, state)) {
								var editForm = instance.get('editForm');

								var formData = instance._getFormData(
									A.IO.stringify(editForm.form), saveAsDraft
								);

								A.io.request(
									Liferay.DDM.FormSettings.autosaveURL,
									{
										after: {
											success: function(event, id, xhr) {
												var responseData = this.get('responseData');

												instance._defineIds(responseData);

												instance.savedState = A.clone(state);

												instance.fire(
													'autosave',
													{
														saveAsDraft: saveAsDraft,
														modifiedDate: responseData.modifiedDate
													}
												);

												if (A.Lang.isFunction(callback)) {
													callback.call();
												}
											}
										},
										data: formData,
										dataType: 'JSON',
										method: 'POST',
										on: {
											failure: function(event, id, xhr) {
												var sessionStatus = Liferay.Session.get('sessionState');

												if (sessionStatus === 'expired' || xhr.status === 401) {
													window.location.reload();
												}
											}
										}
									}
								);
							}
							else if (A.Lang.isFunction(callback)) {
								callback.call();
							}
						}
					},

					_createFormURL: function() {
						var instance = this;

						var formURL;

						var settingsDDMForm = Liferay.component('settingsDDMForm');

						var requireAuthenticationField = settingsDDMForm.getField('requireAuthentication');

						if (requireAuthenticationField.getValue()) {
							formURL = Liferay.DDM.FormSettings.restrictedFormURL;
						}
						else {
							formURL = Liferay.DDM.FormSettings.sharedFormURL;
						}

						var formInstanceId = instance.byId('formInstanceId').val();

						return formURL + formInstanceId;
					},

					_createPreviewURL: function() {
						var instance = this;

						var formURL = instance._createFormURL();

						return formURL + '/preview';
					},

					_defineIds: function(response) {
						var instance = this;

						var formInstanceIdNode = instance.byId('formInstanceId');

						var ddmStructureIdNode = instance.byId('ddmStructureId');

						if (formInstanceIdNode && formInstanceIdNode.val() === '0') {
							formInstanceIdNode.val(response.formInstanceId);
						}

						if (ddmStructureIdNode.val() === '0') {
							ddmStructureIdNode.val(response.ddmStructureId);
						}
					},

					_fillRuleDraft: function() {
						var instance = this;

						var ruleBuilder = instance.get('ruleBuilder');
						var ruleDraft = ruleBuilder.get('ruleDraft');

						if ((!ruleBuilder.isRuleDraftEmpty(ruleDraft)) || (typeof ruleDraft == 'undefined')) {
							ruleBuilder.renderRule();
						}
					},

					_getDescription: function() {
						var instance = this;

						var editor = instance._getDescriptionEditor();

						return editor.getHTML();
					},

					_getDescriptionEditor: function() {
						var instance = this;

						return window[instance.ns('descriptionEditor')];
					},

					_getFormData: function(formString, saveAsDraft) {
						var instance = this;

						var formObject = A.QueryString.parse(formString);

						var state = instance.getState();

						formObject[instance.ns('name')] = JSON.stringify(state.name);

						if (instance._isFormView()) {
							formObject[instance.ns('published')] = JSON.stringify(instance.get('published'));
						}

						formObject[instance.ns('saveAsDraft')] = saveAsDraft;

						return A.QueryString.stringify(formObject);
					},

					_getFormInstanceId: function() {
						var instance = this;

						return instance.byId('formInstanceId').val();
					},

					_getName: function() {
						var instance = this;

						var editor = instance._getNameEditor();

						return editor.getHTML();
					},

					_getNameEditor: function() {
						var instance = this;

						return window[instance.ns('nameEditor')];
					},

					_showPublishAlert: function() {
						var instance = this;

						var publishMessage = Liferay.Language.get('the-form-was-published-successfully-access-it-with-this-url-x');

						var formUrl = instance._createFormURL();

						var span = '<span style="font-weight: 500"><a href=' + formUrl + ' target="_blank">' + formUrl + '</a></span>';

						publishMessage = publishMessage.replace(/\{0\}/gim, span);

						instance._showAlert(publishMessage, 'success');
					},

					_showUnpublishAlert: function() {
						var instance = this;

						instance._showAlert(Liferay.Language.get('the-form-was-unpublished-successfully'), 'success');
					},

					_hideFormBuilder: function() {
						var instance = this;

						instance.one('#formBuilder').hide();

						A.one('.ddm-form-builder-buttons').addClass('hide');

						instance.get(STR_TRANSLATION_MANAGER).hide();
					},

					_hideRuleBuilder: function() {
						var instance = this;

						var ruleBuilder = instance.get('ruleBuilder');

						var ruleBuilderNode = ruleBuilder.get('boundingBox');

						var ruleBuilderAncestorNode = ruleBuilderNode.ancestor();

						ruleBuilderAncestorNode.addClass('hide');

						A.one('.portlet-forms').removeClass('liferay-ddm-form-rule-builder');
					},

					_isFormView: function() {
						var instance = this;

						return instance.get('view') === 'forms';
					},

					_isSameState: function(state1, state2) {
						var instance = this;

						return Liferay.Util.isEqual(
							state1,
							state2,
							function(value1, value2, key) {
								return (key === 'instanceId') || undefined;
							}
						);
					},

					_onBack: function(event) {
						var instance = this;

						if (!instance._isSameState(instance.getState(), instance.savedState)) {
							event.preventDefault();
							event.stopPropagation();

							instance.openConfirmationModal(
								function(dialog) {
									window.location.href = event.currentTarget.get('href');

									dialog.hide();
								},
								function(dialog) {
									dialog.hide();
								}
							);
						}
					},

					_onDescriptionEditorChange: function(event) {
						var instance = this;

						var editingLanguageId = instance.get('editingLanguageId');
						var localizedDescription = instance.get('localizedDescription');

						var descriptionEditor = instance._getDescriptionEditor();

						localizedDescription[editingLanguageId] = descriptionEditor.getHTML();
					},

					_onDestroyPortlet: function(event) {
						var instance = this;

						instance.destroy();
					},

					_onFormButtonClick: function() {
						var instance = this;

						if (instance._currentTab == 'formBuilder') {
							return;
						}

						instance._currentTab = 'formBuilder';

						instance._hideRuleBuilder();

						instance._showFormBuilder();

						instance._addFieldButton();
					},

					_onNameEditorChange: function(event) {
						var instance = this;

						var editingLanguageId = instance.get('editingLanguageId');
						var localizedName = instance.get('localizedName');

						var nameEditor = instance._getNameEditor();

						localizedName[editingLanguageId] = nameEditor.getHTML();
					},

					_onPreviewButtonClick: function() {
						var instance = this;

						instance._autosave(
							true,
							function() {
								var previewURL = instance._createPreviewURL();

								window.open(previewURL, '_blank');
							}
						);
					},

					_onPublishButtonClick: function(event) {
						var instance = this;

						event.preventDefault();

						var publishButton = instance.one('#publish');

						publishButton.html(Liferay.Language.get('publishing'));

						var editForm = instance.get('editForm');

						editForm.formNode.setAttribute('action', Liferay.DDM.FormSettings.publishFormInstanceURL)

						instance.submitForm();

					},

					_onPublishIconClick: function() {
						var instance = this;

						if (!instance.get('published')) {
							return;
						}

						instance._copyPublishFormURLPopover.set('publishURL', instance._createFormURL());

						instance._copyPublishFormURLPopover.show();
					},

					_onRuleBuilderLoaded: function() {
						var instance = this;

						instance.renderUI();
						instance.bindUI();

						instance.savedState = A.clone(instance.getState());
					},

					_onRulesButtonClick: function() {
						var instance = this;

						if (instance._currentTab == 'ruleBuilder') {
							return;
						}

						instance._currentTab = 'ruleBuilder'

						instance._hideFormBuilder();

						instance._showRuleBuilder();

						instance._addRuleButton();

						instance._fillRuleDraft();
					},

					_onSaveButtonClick: function(event) {
						var instance = this;

						event.preventDefault();

						var saveButton = instance.one('#save');

						saveButton.html(Liferay.Language.get('saving'));

						instance.submitForm();
					},

					_setDescription: function(value) {
						var instance = this;

						var editor = instance._getDescriptionEditor();

						editor.setHTML(value);
					},

					_setName: function(value) {
						var instance = this;

						var editor = instance._getNameEditor();

						editor.setHTML(value);
					},

					_setPublished: function(value) {
						var instance = this;

						if (instance._isFormView()) {
							var title;

							if (value) {
								title = Liferay.Language.get('copy-url');
							}
							else {
								title = Liferay.Language.get('publish-the-form-to-get-its-shareable-link');
							}

							var publishIcon = A.one('.publish-icon');

							publishIcon.toggleClass('disabled', !value);
							publishIcon.attr('title', title);
						}
					},

					_showAlert: function(message, type) {
						var instance = this;

						var alert = instance.get('alert');

						if (alert) {
							alert._alertsContainer._node.innerHTML = "";
						}

						var icon = 'exclamation-full';

						if (type === 'success') {
							icon = 'check';
						}

						alert = new Liferay.Alert(
							{
								closeable: true,
								delay: {
									hide: 3000,
									show: 0
								},
								icon: icon,
								message: message,
								type: type
							}
						);

						if (!alert.get('rendered')) {
							alert.render('.management-bar-default .container-fluid-1280');
						}

						alert.show();

						instance.set('alert', alert);
					},

					_showFormBuilder: function() {
						var instance = this;

						Liferay.fire('showFormBuilder');

						instance.one('#formBuilder').show();

						A.one('.ddm-form-builder-buttons').removeClass('hide');

						A.one('.lfr-ddm-plus-button').removeClass('hide');

						instance.get(STR_TRANSLATION_MANAGER).show();
					},

					_showRuleBuilder: function() {
						var instance = this;

						var ruleBuilder = instance.get('ruleBuilder');

						var ruleBuilderNode = ruleBuilder.get('boundingBox');

						var ruleBuilderAncestorNode = ruleBuilderNode.ancestor();

						if (ruleBuilderAncestorNode.hasClass('hide')) {
							ruleBuilderAncestorNode.removeClass('hide');
						}
						else {
							ruleBuilder.show();
						}

						if (!A.one('.form-builder-rule-builder-container')) {
							A.one('.lfr-ddm-plus-button').addClass('hide');
						}

						A.one('.portlet-forms').addClass('liferay-ddm-form-rule-builder');
					},

					_syncDescription: function() {
						var instance = this;

						var defaultLanguageId = instance.get('defaultLanguageId');
						var editingLanguageId = instance.get('editingLanguageId');

						var localizedDescription = instance.get('localizedDescription');

						var description = localizedDescription[editingLanguageId] || localizedDescription[defaultLanguageId];

						localizedDescription[editingLanguageId] = description;

						instance._setDescription(description);
					},

					_syncName: function() {
						var instance = this;

						var defaultLanguageId = instance.get('defaultLanguageId');
						var editingLanguageId = instance.get('editingLanguageId');

						var localizedName = instance.get('localizedName');

						var name = localizedName[editingLanguageId] || localizedName[defaultLanguageId];

						localizedName[editingLanguageId] = name;

						instance._setName(name);
					},

					_updateAutosaveBar: function(savedAsDraft, modifiedDate) {
						var instance = this;

						var message = '';

						if (savedAsDraft) {
							message = Liferay.Language.get('draft-x');
						}
						else {
							message = Liferay.Language.get('saved-x');
						}

						var autosaveMessage = A.Lang.sub(
							message,
							[
								modifiedDate
							]
						);

						instance.one('#autosaveMessage').set('innerHTML', autosaveMessage);
					}
				}
			}
		);

		Liferay.namespace('DDM').FormPortlet = FormPortlet;
	},
	'',
	{
		requires: ['aui-tooltip', 'io-base', 'liferay-alert', 'liferay-ddm-form-builder', 'liferay-ddm-form-builder-copy-publish-form-url-popover', 'liferay-ddm-form-builder-definition-serializer', 'liferay-ddm-form-builder-layout-serializer', 'liferay-ddm-form-builder-rule-builder', 'liferay-portlet-base', 'liferay-util-window', 'querystring-parse']
	}
);