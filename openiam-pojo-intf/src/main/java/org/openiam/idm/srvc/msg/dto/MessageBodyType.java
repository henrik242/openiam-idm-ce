package org.openiam.idm.srvc.msg.dto;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "MessageBodyType")
@XmlEnum
public enum MessageBodyType {
    HTML,
    PLAIN
}
