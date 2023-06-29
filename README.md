

# 知策政策检索系统 ZhiCeSearch

![ZhiCeSearch](https://github.com/BaoWHO/Dungeon/blob/main/Images/ZhiCeSearch.jpg)

## ES 索引字段表

| 字段名           | 类型    | 描述                     |
| ---------------- | ------- | ------------------------ |
| id               | keyword | 政策ID                   |
| title            | text    | 政策标题                 |
| grade            | keyword | 级别                     |
| agency_id        | keyword | 发布机构ID               |
| agency           | text    | 发布机构                 |
| agency_full_name | text    | 发布机构标准名称（全称） |
| pub_number       | keyword | 发文字号                 |
| pub_time         | date    | 发布时间                 |
| type             | keyword | 政策种类                 |
| body             | text    | 正文内容                 |
| province         | text    | 省份                     |
| city             | text    | 地市                     |
| county           | text    | 县区                     |
| source           | text    | 来源                     |
| update_date      | date    | 更新时间                 |

## MySql

### user表

| 字段名     | 类型     |
| ---------- | -------- |
| id         | bigint   |
| phone      | varchar  |
| password   | varchar  |
| nick_name  | varchar  |
| gender     | char     |
| email      | varchar  |
| career     | varchar  |
| longitude  | decimal  |
| latitude   | decimal  |
| create_at  | datetime |
| updated_at | datetime |

### policy表

| 字段             | 类型     |
| ---------------- | -------- |
| id               | bigint   |
| title            | varchar  |
| grade            | varchar  |
| agency_id        | varchar  |
| agency           | varchar  |
| agency_full_name | varchar  |
| pub_number       | varchar  |
| pub_time         | datetime |
| type             | varchar  |
| province         | varchar  |
| city             | varchar  |
| source           | varchar  |
| county           | varchar  |
| create_at        | datetime |
| updated_at       | datetime |
| view             | int      |

## 接口

### 登陆

**POST:** http://117.88.46.6:8888/api/user/login

**输入**

```json
{
    "phone":"17377821609",
    "password":"123456"
}
```

**返回**

```json
{
	"status": "success",
	"data": {
		"data": {
			"id": 8,
			"phone": "18214900204",
			"password": "*",
			"nickName": "HHY",
			"gender": "男",
			"email": "2926539986@qq.com",
			"career": "学生",
			"longitude": 7.09,
			"latitude": 10.06,
			"createAt": "2023-04-09T13:54:55.000+00:00",
			"updatedAt": "2023-04-09T13:54:55.000+00:00"
		},
		"token": "62eb514b-41ad-347f-a174-aeb2def7bb48"
	}
}

{
	"status": "failed",
	"data": {
		"errCode": 20003,
		"errMsg": "手机号或密码错误"
	}
}
```

### 测试是否登陆

**POST**: http://117.88.46.6:8888/api/user/is_login

**输入**

```json
{
    "token": "62eb514b-41ad-347f-a174-aeb2def7bb48"
}
```

**返回**

```json
{
	"status": "success",
	"data": "已登陆"
}

{
	"status": "failed",
	"data": {
		"errCode": 20004,
		"errMsg": "未登录"
	}
}
```

### 注册

**POST:** http://117.88.46.6:8888/api/user/register

**输入**

```json
{
    "phone":"2222222",
    "password":"123456",
    "nickName":"第四",
    "gender":"男",
    "career": "学生",
    "email": "98746621",
    "longitude": "12.0",
    "latitude": "12.0"
}
```

**输出**

```json
{
	"status": "success",
	"data": "注册成功"
}
```

### 登出

**POST/GET:** http://117.88.46.6:8888/api/user/loginout

**无输入**

```

```

**返回**

```json
{
	"status": "success",
	"data": "登出成功"
}
```

### 注销

**POST:** http://117.88.46.6:8888/api/user/unsubscribe

**输入**

```json
{
    "token": "62eb514b-41ad-347f-a174-aeb2def7bb48"
}
```

**返回**

```json
{
	"status": "success",
	"data": "注销成功"
}
```

### 简单搜索

**POST:** http://117.88.46.6:8888/api/policy/search

**输入**

```json
{
	"keyword": "内蒙古",
    "page_no": 1,
    "page_size": 10,
    "token": "62eb514b-41ad-347f-a174-aeb2def7bb48"
}
```

**返回**

```json
{
	"status": "success",
    "data": {
        "total": 10000,
        "data": [
            {
                "id": "100152763",
                "title": "<font color='red'>内蒙古</font>自治区农牧厅<font color='red'>内蒙古</font>自治区林业和草原局<font color='red'>内蒙古</font>自治区发展改革委<font color='red'>内蒙古</font>自治区财政厅关于认定<font color='red'>内蒙古</font>自治区特色农畜产品优势区（第二批）的通知",
                "no_html_title": "内蒙古自治区质量和标准化研究院2022年预算公开",
                "grade": "省级",
                "agency_id": "3000000625",
                "agency": "自治区农牧厅",
                "agency_full_name": "内蒙古自治区农牧厅",
                "pub_number": "内农牧市发〔2020〕245号",
                "pub_time": "2020/08/12",
                "type": "通知",
                "body": "10个地区为<font color='red'>内蒙古</font>特色农畜产品优势区(第二批)(以下简称“<font color='red'>内蒙古</font>特优区”，名单见附件)。",
                "province": "内蒙古自治区",
                "city": null,
                "county": null,
                "source": "内蒙古自治区农牧厅",
                "update_date": "2022/07/07",
                "view": 1
            },
            {
                "id": "100135063",
                "title": "关于征集“爱上<font color='red'>内蒙古</font>，讲好<font color='red'>内蒙古</font>故事”主题广播电视作品的通知",
                "grade": "省级",
                "agency_id": "3000000241",
                "agency": "自治区广电局",
                "agency_full_name": "内蒙古自治区广播电视局",
                "pub_number": "内广发〔2020〕84号",
                "pub_time": "2020/04/13",
                "type": "通知",
                "body": "各盟市广播电视行政管理部门，各广播电视节目制作经营机构：为深入贯彻落实布小林主席对广播电视工作重要指示精神，围绕“爱上<font color='red'>内蒙古</font>，讲好<font color='red'>内蒙古</font>故事”主题，推出更多、更好地展现<font color='red'>内蒙古</font>品牌形象的广播电视作品，<font color='red'>内蒙古</font>自治区广播电视局决定面向全区广播电视节目制作经营机构征集广播电视作品",
                "province": "内蒙古自治区",
                "city": null,
                "county": null,
                "source": "内蒙古自治区广播电视局",
                "update_date": "2022/07/07",
                "view": 1
            }
        ]
    }
}
```

### 搜索建议

**GET:** http://117.88.46.6:8888/api/policy/search_suggest

**输入**

```
http://117.88.46.6:8888/api/policy/search_suggest?prefix=朝阳区
```

**返回**

```json
{
	"status": "success",
	"data": [
		{
			"id": 13526,
			"title": "北京市朝阳区人民政府办公室转发区教委关于2019年本市外区户籍无房家庭适龄儿童在朝阳区接受义务教育证明证件材料审核实施细则的通知",
			"htmlTitle": "北京市<strong>朝阳区</strong>人民政府办公室转发区教委关于2019年本市外区户籍无房家庭适龄儿童在<strong>朝阳区</strong>接受义务教育证明证件材料审核实施细则的通知"
		},
		{
			"id": 13527,
			"title": "北京市朝阳区人民政府办公室转发区教委关于2019年非本市户籍适龄儿童少年在朝阳区接受义务教育证明证件材料审核实施细则的通知",
			"htmlTitle": "北京市<strong>朝阳区</strong>人民政府办公室转发区教委关于2019年非本市户籍适龄儿童少年在<strong>朝阳区</strong>接受义务教育证明证件材料审核实施细则的通知"
		}
	]
}
```

### 具体文档

**POST:** http://117.88.46.6:8888/api/policy/get_policy

**输入**

```json
{
    "id": "100329375",
    "token": "62eb514b-41ad-347f-a174-aeb2def7bb48"
}
```

**返回**

```json
{
	"status": "success",
	"data": {
		"id": "100329375",
		"title": "关于公布2017年内蒙古自治区研究生联合培养基地的通知",
		"grade": "省级",
		"agency_id": "3000000249",
		"agency": "自治区教育厅",
		"agency_full_name": "内蒙古自治区教育厅",
		"pub_number": "内教研字〔2018〕1号",
		"pub_time": "2018/07/09",
		"type": "通知",
		"body": "各研究生培养单位：按照《关于做好内蒙古自治区研究生联合培养基地建设申报和总结工作的通知》（内教研函〔2017〕14号）要求，在各研究生培养单位推荐的基础上，经研究，我厅确定了11个2017年自治区研究生联合培养基地。请各研究生培养单位以研究生联合培养基地建设为抓手，大力推进研究生教育改革，进一步探索和完善校所（院）、校企协同创新和联合培养研究生的机制和模式，不断提高研究生创新、创造和创业的能力和水平。要高度重视研究生联合培养基地项目建设工作，切实加强管理，不断加大投入，合理使用经费，确保项目建设的质量和效益。附件：2017年内蒙古自治区研究生联合培养基地名单.docx内蒙古自治区教育厅2018年1月8日",
		"province": "内蒙古自治区",
		"city": null,
        "county": null,
		"source": "内蒙古自治区教育厅",
		"update_date": "2022/07/07",
        "view": 1
	}
}
```

### 精准搜索接口

**POST:** http://117.88.46.6:8888/api/policy/exact_search

**输入**

| 字段      | 取值                                        | 描述                                              |
| --------- | ------------------------------------------- | ------------------------------------------------- |
| grade     | *、country、region(province,city,county)    | 等级，可以选择全部、国家级、地区级                |
| province  | *、湖南省、内蒙古自治区、北京市、【其他】   | 省份，选择了地区级才能选择                        |
| city      | *、长沙市、呼和浩特市、锡林郭勒盟、【其他】 | 城市，选择了地区级才能选择                        |
| county    | *、多伦县、潜江市、镶黄旗、【其他】         | 区县，选择了地区级才能选择                        |
| agency    | *、内蒙古自治区公安厅、【其他】             | 部门，可选择的部门根据以上4个选项更新到搜索下拉框 |
| type      | *、公告、公示、函、【其他】                 | 政策种类                                          |
| keyword   | 内蒙古的研究院、【其他】                    | 搜索关键词                                        |
| sort_by   | default、time                               | 排序方式，可以选择默认排序、时间排序              |
| time_from | *、2018/08/27、【其他】                     | 起始时间                                          |
| time_to   | *、2020/5/1、【其他】                       | 截止时间                                          |
| page_no   | 1、【其他】                                 | 页号                                              |
| page_size | 10、【其他】                                | 页大小                                            |

```json
// 搜索国家级与地区级（即所有等级）
{
    "grade": "*",				//所有等级（国家级与地区级）
    "province": "*",			//所有省份，只能为*
    "city": "*",				//所有城市，只能为*
    "county": "*",				//所有区县，只能为*
    "agency": "*",				//所有机构，只能为*
    "type": "函",				//政策类型，字段取值可以为*、函、通知等等
    "keyword": "内蒙古",
    "sort_by": "default",		//排序方式，字段取值包括default、time，即默认排序与时间排序
    "time_from": "2020/9/21",	//起始时间，字段格式为yyyy/mm/dd，可以有前导零，可以为*
    "time_to": "2021/09/21",	//截止时间，字段格式为yyyy/mm/dd，可以有前导零，可以为*
    							//起始与截止时间，可以省略两个，可以省略一个，可以两个都写
    "page_no": 1,				//第几页
    "page_size": 10				//每页政策条数
    "token": "62eb514b-41ad-347f-a174-aeb2def7bb48"
}
```

```json
// 搜索国家级
{
    "grade": "county",			//国家级
    "province": "*",			//所有省份，只能为*
    "city": "*",				//所有城市，只能为*
    "county": "*",				//所有区县，只能为*
    "agency": "自治区水利厅",		//机构，字段取值可以为*、自治区发展改革委、自治区水利厅
    "type": "通知",				//类型，字段取值可以为*、函、通知等等
    "keyword": "内蒙古",
    "sort_by": "default",		//默认排序，包括default、time，即默认排序与时间排序
    "time_from": "2020/09/21",	//省略起始时间，格式为yyyy/mm/dd，可以有前导零
    "time_to": "2021/09/21"		//省略截止时间，格式为yyyy/mm/dd，可以有前导零
    							//起始与截止时间，可以省略两个，可以省略一个，可以两个都写
    "page_no": 1,				//第几页
    "page_size": 10				//每页政策条数
    "token": "62eb514b-41ad-347f-a174-aeb2def7bb48"
}
```

```json
// 搜索地区级
{
    "grade": "region",			//地区级
    "province": "内蒙古自治区",	//省份
    "city": "鄂尔多斯市",		//城市
    "county": "*",				//区县
    "agency": "自治区水利厅",		//机构，字段取值可以为*、自治区发展改革委、自治区水利厅
    "type": "通知",				//类型，字段取值可以为*、函、通知等等
    "keyword": "内蒙古",
    "sort_by": "default",		//默认排序，包括default、time，即默认排序与时间排序
    "time_from": "2020/09/21",	//省略起始时间，格式为yyyy/mm/dd，可以有前导零
    "time_to": "2021/09/21"		//省略截止时间，格式为yyyy/mm/dd，可以有前导零
    							//起始与截止时间，可以省略两个，可以省略一个，可以两个都写
    "page_no": 1,				//第几页
    "page_size": 10				//每页政策条数
    "token": "62eb514b-41ad-347f-a174-aeb2def7bb48"
}
```

**返回**

```json
// 与之前类似
{
    "status": "success",
    "data": {
        "total": 10000,
        "data": [
            {
                ......
            }
        ]
    }
}
```
### 获取热榜

**GET:** http://117.88.46.6:8888/api/policy/get_rank

**无输入**

```

```

**返回**

```json
//按照榜一、榜二、榜三······的顺序返回热榜前十的政策信息(与之前类似是政策的所有信息，前端只需要展示标题即可)
```

### 具体热文档(点击排行榜)
**POST:** http://117.88.46.6:8888/api/policy/get_hotpolicy

**输入**

```json
id表示政策id，r表示热榜第几名
{
    "id": "100329375",
    "r": 1,
    "token": "62eb514b-41ad-347f-a174-aeb2def7bb48"
}
```

**返回**

```
//与之前的返回类似
```

### 推荐政策

已登录用户可以正常推荐，未登录用户只返回热榜

**POST**: http://117.88.46.6:8888/api/policy/recommend

**输入**

```json
{
    "size": 10,
    "token": "62eb514b-41ad-347f-a174-aeb2def7bb48"
}
```

**返回**

```
//与之前类似
```

### 部门搜索建议

**GET:** http://117.88.46.6:8888/api/agency/agency_suggest

**输入**

```
http://117.88.46.6:8888/api/agency/agency_suggest?prefix=辽宁日
```

**返回**

```json
{
	"status": "success",
	"data": [
		{
			"id": 2236,
			"agency_full_name": "辽宁日报",
			"html_agency_full_name": "<strong>辽宁日</strong>报"
		}
	]
}
```

### 部门筛选

**POST:** http://117.88.46.6:8888/api/agency/agency_filter

**输入**

```json
{
	"grade": "国家级",		//国家级、地区级
    "province": "内蒙古自治区"
    //province、city、county 三个可选项
}
```

**返回**

```json
{
	"status": "success",
	"data": [
		{
			"value": "*",
			"label": "全部部门机关"
		},
		{
			"value": "新华网",
			"label": "新华网"
		},
		{
			"value": "国家科学技术奖励工作办公室",
			"label": "国家科学技术奖励工作办公室"
		}
	]
}
```

### 地区接口

**POST:** http://117.88.46.6:8888/api/district/get_district

**输入**

```json
{
    "province": "内蒙古自治区",
    "city": "*",
    "county": "*"
}
```

**返回**

```json
{
	"status": "success",
	"data": {
		"province": [
			"内蒙古自治区"
		],
		"city": [
			"呼和浩特市",
			"包头市",
			"乌海市",
			"赤峰市",
			"通辽市",
			"鄂尔多斯市",
			"呼伦贝尔市",
			"巴彦淖尔市",
			"乌兰察布市",
			"兴安盟",
			"锡林郭勒盟",
			"阿拉善盟"
		],
		"county": []
	}
}
```

### 获得所有地区数据

**POST/GET:** http://117.88.46.6:8888/api/district/get_all

**无输入**

```

```

**返回**

```json
{
    "":""
}
```

### 相关搜索

**GET:** http://117.88.46.6:8888/api/policy/other_words

**输入**

```
http://117.88.46.6:8888/api/policy/other_words?keyword=湖南中医药大学
```

**返回**

```json
{
	"status": "success",
	"data": [
		"湖南中医药大学研招网",
		"湖南中医药大学官网",
		"湖南中医药高等学校官网",
		"湖南医药大学",
		"湖南中医药大学校花",
		"福建中医药大学厉害吗",
		"湖南中医药大学历任校长",
		"湖南中医药大学专升本",
		"湖南中医药大学教务管理系统",
		"湖南中医药大学招生"
	]
}
```

