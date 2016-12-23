
DROP TABLE  IF EXISTS `t_hotdeal`;
create table t_hotdeal (
    id bigint(20)  not null auto_increment primary key,
    createTime datetime,
    sourceUrl varchar(255),
    title varchar(255),
    refprice float
 ) DEFAULT CHARSET=utf8;

DROP TABLE  IF EXISTS `t_material`;
CREATE TABLE `t_material` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) NOT NULL COMMENT '标题',
  `subTitle` varchar(45) DEFAULT NULL COMMENT '副标题',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `btnText` varchar(45) DEFAULT NULL COMMENT '按钮文字',
  `openWay` varchar(10) DEFAULT NULL COMMENT '打开方式',
  `price` float DEFAULT NULL COMMENT '价格',
  `url` varchar(255) DEFAULT NULL COMMENT '链接',
  `putCountry` varchar(200) DEFAULT NULL COMMENT '投放国家',
  `icon` varchar(255) DEFAULT NULL COMMENT '投放的icon,需要png/jpg/jpeg,小于150kb',
  `putPlatform` varchar(10) DEFAULT NULL COMMENT '投放平台:android/ios',
  `minVersion` int(1) DEFAULT NULL COMMENT '平台最低版本',
  `maxVersion` int(1) DEFAULT NULL COMMENT '平台最高版本',
  `appType` varchar(200) DEFAULT NULL COMMENT '投放app的类型',
  `settlementWay` varchar(10) DEFAULT NULL COMMENT '具体结算方式(CPI/CPC/CPM/CPA)',
  `dailyRunning` varchar(45) DEFAULT NULL COMMENT '每日跑量',
  `pvRequestUrl` varchar(255) DEFAULT NULL COMMENT 'pv地址',
  `tags` VARCHAR(200) NULL COMMENT '标签',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='素材表';


DROP TABLE IF EXISTS `t_equipment`;
CREATE TABLE `t_equipment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `androidId` varchar(45) DEFAULT NULL,
  `tags` varchar(200) DEFAULT NULL COMMENT '标签',
  `createTime` DATETIME DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_material_creative`;
CREATE TABLE `t_material_creative` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `materialId` INT NOT NULL,
  `url` VARCHAR(255) NULL,
  `width` VARCHAR(45) NULL,
  `height` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COMMENT = '素材图';


DROP TABLE IF EXISTS `t_tag_statistical`;
CREATE TABLE `t_tag_statistical` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `androidid` VARCHAR(45) NULL,
  `xiaomi` INT NULL,
  `lenovo` INT NULL,
  `redmi` INT NULL,
  `huawei` INT NULL,
  `honor` INT NULL,
  `samsung` INT NULL,
  `meizu` INT NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COMMENT = '设备统计表';


DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `androidid` varchar(45) DEFAULT NULL,
  `channel` varchar(45) NOT NULL COMMENT '渠道',
  `orderId` varchar(45) NOT NULL,
  `orderTime` datetime NOT NULL COMMENT '下单时间',
  `saleAmount` decimal(10,2) NOT NULL COMMENT '订单金额',
  `commissionRate` decimal(10,2) DEFAULT NULL COMMENT '佣金比例',
  `commission` decimal(10,2) DEFAULT NULL COMMENT '佣金',
  `website` varchar(255) DEFAULT NULL COMMENT '售卖网站',
  `orderStatus` varchar(45) DEFAULT NULL COMMENT '订单状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `t_tag` (
  `id`      INT         NOT NULL AUTO_INCREMENT,
  `aid`     VARCHAR(45) NOT NULL,
  `ymd`     VARCHAR(45) NOT NULL,
  `samsung` INT         NULL,
  `xiaomi`  INT         NULL,
  `redmi`   INT         NULL,
  `moto`    INT         NULL,
  `leeco`   INT         NULL,
  `lenovo`  INT         NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;









