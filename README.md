# mydata
需要构造关系描述文档
示例：example/example.xlsx

根据关系描述文档生成sql脚本:
1. 安装java 1.8
2. 将mydata-1.1-bin.tar解压到当前目录
3. 配置文件config/mydata.properties
4. 运行 java -jar mydata-1.1.jar > result.sql
5. 结束

#提供的方法
+ int intRand()  
*生成int正随机整数*
+ int intRand(int n)  
*生成0～n的随机整数* 
+ int intRand(int start, int end)  
*生成start～end的随机整数*  
+ String communityOfBeiJing()  
*随机生成北京社区*
+ String dateBetween(String formatStr,String start,int days)  
*formatStr 目前只支持 "yyyy-MM-dd"*
+ String dateBetween(String formatStr,String start,String end)  
*formatStr 目前只支持 "yyyy-MM-dd"*
+ String dateOfMarriage(String birthDayOfMan,String deathDayOfMan,String birthDayOfWoman,String deathDayOfWoman)  
*所有参数目前只支持 "yyyy-MM-dd" 时间格式*
+ String dateRand(int dateType)  
*dateType取值范围 1或2：1表示生成时间格式为 yyyy-MM-dd; 2表示生成时间格式为 yyyy-MM-dd HH:mm:ss*
+ String dic(String key)  
*key取值范围为dictionaries/test.dic文档内容的左值，返回文档中对应右值的随机一个* 
+ String idCardRand(int digit)  
*随机生成身份证号码，0代表18位, 1代表15位*
+ String myAddressRand()  
*随机生成地址*
+ String nameRand()  
*随机生成姓名*
+ String numRand(int n)  
*生成n位随机数*  
+ String phoneNumberRand()  
*随机生成电话号码*
+ String policeOfBeiJing()  
*随机获取北京派出所*