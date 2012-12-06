/*
 * Copyright 2009, OpenIAM LLC 
 * This file is part of the OpenIAM Identity and Access Management Suite
 *
 *   OpenIAM Identity and Access Management Suite is free software: 
 *   you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License 
 *   version 3 as published by the Free Software Foundation.
 *
 *   OpenIAM is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   Lesser GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with OpenIAM.  If not, see <http://www.gnu.org/licenses/>. *
 */

/**
 * 
 */
package org.openiam.idm.srvc.msg.service;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author suneet
 *
 */
public class Message {
	public enum BodyType {
        PLAIN_TEXT,
        HTML_TEXT
    }
    private List<InternetAddress> to = new LinkedList<InternetAddress>();
    private List<InternetAddress> cc = new LinkedList<InternetAddress>();
    private List<InternetAddress> bcc = new LinkedList<InternetAddress>();
    private InternetAddress from;
    private String subject;
    private String body;
    private BodyType bodyType = BodyType.PLAIN_TEXT;
    private List<String> attachments = new LinkedList<String>();

	private static final Log log = LogFactory.getLog(Message.class);

	public List<InternetAddress> getTo() {
		return to;
	}
	public void addTo(String to) {
		try{
			this.to.add(new InternetAddress(to));
		}catch(AddressException ae) {
			log.error(ae);
		}
	}
	public InternetAddress getFrom() {
		return from;
	}
	public void setFrom(String fr) {
		try {
			this.from = new InternetAddress(fr);
		}catch(AddressException ae) {
			log.error(ae);
		}

	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public List<InternetAddress> getCc() {
		return cc;
	}
	public void addCc(String cc) {
		try {
			this.cc.add(new InternetAddress(cc));
		}catch(AddressException ae) {
			log.error(ae);
		}
	}

	public List<InternetAddress> getBcc() {
		return bcc;
	}

	public void addBcc(String bcc) {

		try {
			this.bcc.add(new InternetAddress(bcc));
		}catch(AddressException ae) {
			log.error(ae);
		}

	}

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public void addAttachments(String attachmentFilePath) {
        this.attachments.add(attachmentFilePath);
    }
}
