package com.tarena.util;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * 使用Hibernate创建表格
 * 
 * @author chenlanqing
 *
 */
public class ExportDB {
	public static void main(String[] args) {
		/*
		 * Configuration config = new Configuration();如果是这么写的话,
		 * 那么加载的配置文件就是早期的hibernate.properties,而不是hibernate.cfg.xml; Configuration config
		 * = new Configuration().configure(); 加载hibernate.cfg.xml配置文件
		 */
		Configuration config = new Configuration().configure();
		/*
		 * 创建建表工具类的对象:SchemaExport
		 */
		SchemaExport export = new SchemaExport(config);
		/*
		 * 调用create方法,生成对应的表格; 该方法第一个参数:是否讲DDL操作语句打印到控制台; 第二个参数:是否将结果应用的到数据库中
		 */
		export.create(true, true);
	}
}
