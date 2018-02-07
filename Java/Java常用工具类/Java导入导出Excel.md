# 一.读写Excel的三种技术：
#### 1.POI:提供对Office格式的档案读写:HSSF		(iText操作PDF或rtf)	
	HSSF :　读取.xls表格
	XSSF :　读取.xlsx表格
	HWPF :  读写Word
	HSLF :  读写powerpoint
	HDGF :  读写visio
#### 2.JXL:读取,创建,修改

#### 3.FastExcel:纯Java开放,支持97-2003文件格式

#### 4.POI与JXL比较
    (1).POI相对效率高;
    (2).POI操作相对复杂;
    (3).POI支持公式,宏,图像图表;JXL只部分支持
    (4).POI能够支持修饰单元格属性;JXL对格式支持不如POI
    (5).支持字体,数字,日期操作

# 二.	JXL读取,创建,修改Excel文件
#### 1.创建Excel文件:
    public class JxlWriteExcel {
    	public static void main(String[] args) {
    		File file = new File("d:/jxl_test.xls");
    		String[] title = {"id","name","gender"};
    		int index = 1000;
    		try {
    			file.createNewFile();
    			// 创建工作薄
    			WritableWorkbook workbook = Workbook.createWorkbook(file);
    			// 创建工作薄表格，createSheet("表名","表的序号")
    			WritableSheet sheet = workbook.createSheet("sheet1", 0);
    			Label label = null;
    			for (int i = 0; i < title.length; i++) {
    				label = new Label(i, 0, title[i]);
    				sheet.addCell(label);
    			}			
    			for(int i = 1; i < 10; i++){
    				label = new Label(0, i, String.valueOf(index++));
    				sheet.addCell(label);
    				label = new Label(1, i, "Coco" + i);
    				sheet.addCell(label);
    				label = new Label(2, i, "male");
    				sheet.addCell(label);
    			}
    			workbook.write();
    			workbook.close();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    }

#### 2.读取:
    public class JxlReadExcel {
    	public static void main(String[] args) {
    		File file = new File("d:/jxl_test.xls");
    		try {
    			Workbook workbook = Workbook.getWorkbook(file);
    			Sheet sheet = workbook.getSheet(0);
    			for (int i = 0; i < sheet.getRows(); i++) {
    				for (int j = 0; j < sheet.getColumns(); j++) {
    					Cell cell = sheet.getCell(j, i);
    					System.out.print(cell.getContents() + "\t");
    				}
    				System.out.println();
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    }

# 三.POI与Excel	
#### 1.生存Excel
    public class PoiExportExcel {
    	public static void main(String[] args) {
    		@SuppressWarnings("resource")
    		HSSFWorkbook workbook = new HSSFWorkbook();
    		HSSFSheet sheet = workbook.createSheet();		
    		HSSFRow row = sheet.createRow(0);
    		HSSFCell cell = null;
    		String[] title = {"id","name","sex"};
    		for (int i = 0; i < title.length; i++) {
    			cell = row.createCell(i);
    			cell.setCellValue(title[i]);
    		}		
    		for (int i = 1; i <= 10; i++) {
    			HSSFRow nextrow = sheet.createRow(i);
    			HSSFCell nextcell = nextrow.createCell(0);
    			nextcell.setCellValue("100" + i);
    			nextcell = nextrow.createCell(1);
    			nextcell.setCellValue("Sam" + i);
    			nextcell = nextrow.createCell(2);
    			if( i % 2 == 0){
    				nextcell.setCellValue("男");
    			}else{
    				nextcell.setCellValue("女");
    			}
    			
    		}
    		
    		File file = new File("d:/poi_test.xls");
    		try {
    			file.createNewFile();
    			FileOutputStream out = FileUtils.openOutputStream(file);
    			workbook.write(out);
    			out.close();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    }	
#### 2.读取Excel:
    public class PoiReadExcel {
    	public static void main(String[] args) throws Exception {
    		File file = new File("d:/poi_test.xls");
    		FileInputStream stream = FileUtils.openInputStream(file);
    		HSSFWorkbook workbook = new HSSFWorkbook(stream);
    		// 获取工作表的方式:一种是按序号,一种是按表名
    //		HSSFSheet sheet = workbook.getSheet("sheet0");
    		HSSFSheet sheet = workbook.getSheetAt(0);
    		int lastRownu = sheet.getLastRowNum();
    		for (int i = 0; i <= lastRownu; i++) {
    			HSSFRow row = sheet.getRow(i);
    			int lastCellNum = row.getLastCellNum();
    			for (int j = 0; j < lastCellNum; j++) {
    				HSSFCell cell = row.getCell(j);
    				String value = cell.getStringCellValue();
    				System.out.print(value + "\t");
    			}
    			System.out.println();
    		}
    	}
    }	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	