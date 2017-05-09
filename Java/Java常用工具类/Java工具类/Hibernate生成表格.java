package com.tarena.util;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
/**
 *	ʹ��Hibernate������� 
 * @author chenlanqing
 *
 */
public class ExportDB {
	public static void main(String[] args) {
		/*
		 * Configuration config = new Configuration();�������ôд�Ļ�,
		 * ��ô���ص������ļ��������ڵ�hibernate.properties,������hibernate.cfg.xml;
		 * Configuration config = new Configuration().configure();
		 * ����hibernate.cfg.xml�����ļ�
		 */
		Configuration config = new Configuration().configure();
		/*
		 * ������������Ķ���:SchemaExport
		 */
		SchemaExport export = new SchemaExport(config);
		/*
		 * ����create����,���ɶ�Ӧ�ı��;
		 * �÷�����һ������:�Ƿ�DDL��������ӡ������̨;
		 * �ڶ�������:�Ƿ񽫽��Ӧ�õĵ����ݿ���
		 */
		export.create(true, true);
	}
}
