/**
 * Copyright (c) 2000-2007 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.portlet.documentlibrary.model.impl;

import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portal.util.PropsUtil;

import com.liferay.util.GetterUtil;
import com.liferay.util.XSSUtil;

import java.io.Serializable;

import java.sql.Types;

import java.util.Date;

/**
 * <a href="DLFileShortcutModelImpl.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class is a model that represents the <code>DLFileShortcut</code> table in
 * the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.documentlibrary.service.model.DLFileShortcut
 * @see com.liferay.portlet.documentlibrary.service.model.DLFileShortcutModel
 * @see com.liferay.portlet.documentlibrary.service.model.impl.DLFileShortcutImpl
 *
 */
public class DLFileShortcutModelImpl extends BaseModelImpl {
	public static String TABLE_NAME = "DLFileShortcut";
	public static Object[][] TABLE_COLUMNS = {
			{ "fileShortcutId", new Integer(Types.BIGINT) },
			{ "companyId", new Integer(Types.BIGINT) },
			{ "userId", new Integer(Types.BIGINT) },
			{ "userName", new Integer(Types.VARCHAR) },
			{ "createDate", new Integer(Types.TIMESTAMP) },
			{ "modifiedDate", new Integer(Types.TIMESTAMP) },
			{ "folderId", new Integer(Types.BIGINT) },
			{ "toFolderId", new Integer(Types.BIGINT) },
			{ "toName", new Integer(Types.VARCHAR) }
		};
	public static String TABLE_SQL_CREATE = "create table DLFileShortcut (fileShortcutId LONG not null primary key,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,folderId LONG,toFolderId LONG,toName VARCHAR(300) null)";
	public static String TABLE_SQL_DROP = "drop table DLFileShortcut";
	public static boolean XSS_ALLOW_BY_MODEL = GetterUtil.getBoolean(PropsUtil.get(
				"xss.allow.com.liferay.portlet.documentlibrary.model.DLFileShortcut"),
			XSS_ALLOW);
	public static boolean XSS_ALLOW_USERNAME = GetterUtil.getBoolean(PropsUtil.get(
				"xss.allow.com.liferay.portlet.documentlibrary.model.DLFileShortcut.userName"),
			XSS_ALLOW_BY_MODEL);
	public static boolean XSS_ALLOW_TONAME = GetterUtil.getBoolean(PropsUtil.get(
				"xss.allow.com.liferay.portlet.documentlibrary.model.DLFileShortcut.toName"),
			XSS_ALLOW_BY_MODEL);
	public static long LOCK_EXPIRATION_TIME = GetterUtil.getLong(PropsUtil.get(
				"lock.expiration.time.com.liferay.portlet.documentlibrary.model.DLFileShortcutModel"));

	public DLFileShortcutModelImpl() {
	}

	public long getPrimaryKey() {
		return _fileShortcutId;
	}

	public void setPrimaryKey(long pk) {
		setFileShortcutId(pk);
	}

	public Serializable getPrimaryKeyObj() {
		return new Long(_fileShortcutId);
	}

	public long getFileShortcutId() {
		return _fileShortcutId;
	}

	public void setFileShortcutId(long fileShortcutId) {
		if (fileShortcutId != _fileShortcutId) {
			_fileShortcutId = fileShortcutId;
		}
	}

	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		if (companyId != _companyId) {
			_companyId = companyId;
		}
	}

	public long getUserId() {
		return _userId;
	}

	public void setUserId(long userId) {
		if (userId != _userId) {
			_userId = userId;
		}
	}

	public String getUserName() {
		return GetterUtil.getString(_userName);
	}

	public void setUserName(String userName) {
		if (((userName == null) && (_userName != null)) ||
				((userName != null) && (_userName == null)) ||
				((userName != null) && (_userName != null) &&
				!userName.equals(_userName))) {
			if (!XSS_ALLOW_USERNAME) {
				userName = XSSUtil.strip(userName);
			}

			_userName = userName;
		}
	}

	public Date getCreateDate() {
		return _createDate;
	}

	public void setCreateDate(Date createDate) {
		if (((createDate == null) && (_createDate != null)) ||
				((createDate != null) && (_createDate == null)) ||
				((createDate != null) && (_createDate != null) &&
				!createDate.equals(_createDate))) {
			_createDate = createDate;
		}
	}

	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		if (((modifiedDate == null) && (_modifiedDate != null)) ||
				((modifiedDate != null) && (_modifiedDate == null)) ||
				((modifiedDate != null) && (_modifiedDate != null) &&
				!modifiedDate.equals(_modifiedDate))) {
			_modifiedDate = modifiedDate;
		}
	}

	public long getFolderId() {
		return _folderId;
	}

	public void setFolderId(long folderId) {
		if (folderId != _folderId) {
			_folderId = folderId;
		}
	}

	public long getToFolderId() {
		return _toFolderId;
	}

	public void setToFolderId(long toFolderId) {
		if (toFolderId != _toFolderId) {
			_toFolderId = toFolderId;
		}
	}

	public String getToName() {
		return GetterUtil.getString(_toName);
	}

	public void setToName(String toName) {
		if (((toName == null) && (_toName != null)) ||
				((toName != null) && (_toName == null)) ||
				((toName != null) && (_toName != null) &&
				!toName.equals(_toName))) {
			if (!XSS_ALLOW_TONAME) {
				toName = XSSUtil.strip(toName);
			}

			_toName = toName;
		}
	}

	public Object clone() {
		DLFileShortcutImpl clone = new DLFileShortcutImpl();
		clone.setFileShortcutId(getFileShortcutId());
		clone.setCompanyId(getCompanyId());
		clone.setUserId(getUserId());
		clone.setUserName(getUserName());
		clone.setCreateDate(getCreateDate());
		clone.setModifiedDate(getModifiedDate());
		clone.setFolderId(getFolderId());
		clone.setToFolderId(getToFolderId());
		clone.setToName(getToName());

		return clone;
	}

	public int compareTo(Object obj) {
		if (obj == null) {
			return -1;
		}

		DLFileShortcutImpl dlFileShortcut = (DLFileShortcutImpl)obj;
		long pk = dlFileShortcut.getPrimaryKey();

		if (getPrimaryKey() < pk) {
			return -1;
		}
		else if (getPrimaryKey() > pk) {
			return 1;
		}
		else {
			return 0;
		}
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		DLFileShortcutImpl dlFileShortcut = null;

		try {
			dlFileShortcut = (DLFileShortcutImpl)obj;
		}
		catch (ClassCastException cce) {
			return false;
		}

		long pk = dlFileShortcut.getPrimaryKey();

		if (getPrimaryKey() == pk) {
			return true;
		}
		else {
			return false;
		}
	}

	public int hashCode() {
		return (int)getPrimaryKey();
	}

	private long _fileShortcutId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private long _folderId;
	private long _toFolderId;
	private String _toName;
}