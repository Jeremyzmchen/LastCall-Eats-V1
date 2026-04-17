# LastCall Eats — 项目启动指南

## 目录
1. [环境要求](#1-环境要求)
2. [拉取代码](#2-拉取代码)
3. [数据库启动与初始化](#3-数据库启动与初始化)
4. [后端启动](#4-后端启动)
5. [前端启动](#5-前端启动)
6. [测试数据注入](#6-测试数据注入)
7. [功能测试清单](#7-功能测试清单)

---

## 1. 环境要求

| 工具 | 版本要求 |
|------|---------|
| Java | 17 或以上 |
| Maven | 3.8 或以上（项目自带 `mvnw`，可不单独安装） |
| MySQL | 8.x |
| Node.js | 18 或以上 |
| Expo Go | 手机安装最新版（App Store / Google Play 搜索 Expo Go） |

---

## 2. 拉取代码

```bash
git clone https://github.com/Jeremyzmchen/LastCall-Eats-V1.git
cd LastCall-Eats-V1
```

---

## 3. 数据库启动与初始化

### 方式 A：已安装 MySQL（Navicat / MySQL Workbench 等）

1. 打开 Navicat，连接本地 MySQL（默认 `localhost:3306`）
2. 新建查询，执行 `sql/schema.sql` 创建数据库和表结构
3. 后续测试数据注入见第 6 节

### 方式 B：命令行（无图形软件）

```bash
# 登录 MySQL
mysql -u root -p

# 执行建表脚本
source /path/to/LastCall-Eats-V1/sql/schema.sql
exit
```

### 方式 C：Docker（未安装 MySQL）

```bash
docker run -d \
  --name lastcall-mysql \
  -e MYSQL_ROOT_PASSWORD=yourpassword \
  -e MYSQL_DATABASE=lastcall_eats \
  -p 3306:3306 \
  mysql:8.0

# 等待约 10 秒后执行建表
mysql -h 127.0.0.1 -u root -p lastcall_eats < sql/schema.sql
```

---

## 4. 后端启动

### 4.1 配置 application-dev.yml

复制示例文件并填写配置：

```bash
cp lastcall-api/src/main/resources/application-dev.yml.example \
   lastcall-api/src/main/resources/application-dev.yml
```

编辑 `application-dev.yml`，填写以下字段：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/lastcall_eats?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root
    password: 你的数据库密码

app:
  jwt:
    secret: 任意一段长度超过32字符的随机字符串

  stripe:
    secret-key: sk_test_你的Stripe测试密钥
    webhook-secret: whsec_你的webhook密钥
```

> Stripe 测试密钥在 [https://dashboard.stripe.com](https://dashboard.stripe.com) → Developers → API Keys 获取。

### 4.2 构建并启动

```bash
# 在项目根目录（pom.xml 所在目录）
./mvnw clean package -DskipTests

# 启动
./mvnw spring-boot:run -pl lastcall-api
```

Windows 用：
```bat
mvnw.cmd spring-boot:run -pl lastcall-api
```

启动成功后终端会显示：
```
Started LastCallEatsApplication in x.xxx seconds
```

后端默认运行在 `http://localhost:8080`。

### 4.3 IDE 启动（IntelliJ IDEA）

1. 打开项目根目录
2. 找到 `lastcall-api/src/main/java/com/lastcalleats/LastCallEatsApplication.java`
3. 右键 → Run

---

## 5. 前端启动

```bash
cd lastcall-frontend/lastcall-app

# 安装依赖（首次）
npm install

# 启动（自动检测本机 IP 并写入 .env.local）
npm run dev
```

> `npm run dev` 会自动获取当前电脑的局域网 IP，写入 `.env.local`，前端会自动连接正确的后端地址。**每次换网络后重新运行一次 `npm run dev` 即可，无需手动改代码。**

启动后终端会显示二维码，用手机 Expo Go 扫码即可打开 App。

**注意：手机和电脑必须在同一个 WiFi 下。**

---

## 6. 测试数据注入

项目提供了完整的测试数据脚本 `sql/seed.sql`。

### 方式 A：Navicat

1. 打开 Navicat → 选择 `lastcall_eats` 数据库
2. 新建查询 → 打开 `sql/seed.sql`
3. 全选 → 运行

### 方式 B：命令行

```bash
mysql -u root -p lastcall_eats < sql/seed.sql
```

### 注入后的测试账号

**用户账号（密码统一：`111111`）**

| 邮箱 | 昵称 |
|------|------|
| alice@example.com | Alice |
| bob@example.com | Bob |
| charlie@example.com | Charlie |

**商家账号（密码统一：`111111`）**

| 邮箱 | 店名 |
|------|------|
| bakery@example.com | Golden Bakery |
| sushi@example.com | Sakura Sushi |
| cafe@example.com | Brew & Bite Cafe |

---

## 7. 功能测试清单

### 用户端

#### 注册 / 登录
- [ ] 注册新用户（邮箱、密码、昵称）
- [ ] 用已注册邮箱重复注册（应提示邮箱已存在）
- [ ] 用户登录
- [ ] 登录后自动跳转到 Browse 页面

#### Browse（浏览商品）
- [ ] 浏览当前可用的限时商品列表
- [ ] 搜索商品名称或商家名称
- [ ] 下拉刷新列表
- [ ] 点击卡片进入商品详情
- [ ] 卡片右上角点击心形收藏 / 取消收藏

#### 商品详情
- [ ] 查看商品详情（名称、商家、价格、取货时间、剩余数量）
- [ ] 右上角心形收藏 / 取消收藏
- [ ] 点击 Reserve 创建订单并跳转支付页面

#### 支付
- [ ] 输入 Stripe 测试 payment method ID：`pm_card_visa`
- [ ] 点击支付后跳转到取货码页面
- [ ] 输入无效 ID 应报错

#### 取货码
- [ ] 显示 6 位数字取货码
- [ ] 显示 QR 二维码
- [ ] 显示订单状态（PAID）

#### Favourites（收藏列表）
- [ ] 查看已收藏的 listing 列表
- [ ] 点击心形取消收藏
- [ ] 下拉刷新

#### Community（社区）
- [ ] 查看社区帖子列表
- [ ] 发布新帖子
- [ ] 帖子可选关联商家

#### Profile（个人资料）
- [ ] 查看个人资料（邮箱、昵称）
- [ ] 修改昵称
- [ ] 退出登录

---

### 商家端

#### 注册 / 登录
- [ ] 注册新商家（邮箱、密码、店名、地址）
- [ ] 商家登录
- [ ] 登录后跳转到商家首页

#### Products — Templates（商品模板）
- [ ] 查看已有模板列表
- [ ] 新建模板（名称、描述、原价）
- [ ] 编辑模板
- [ ] 删除模板

#### Products — Listings（限时上架）
- [ ] 查看今日上架列表
- [ ] 新建 listing（选择模板、折扣价、数量、取货时间、日期）
- [ ] 下架 listing（点击删除按钮）

#### Orders（订单管理）
- [ ] 查看所有用户订单
- [ ] 订单显示状态（PENDING_PAYMENT / PAID）

#### 核销取货码
- [ ] 输入用户的 6 位数字取货码或扫 QR 码完成核销

#### Profile（商家资料）
- [ ] 查看商家资料（邮箱、店名、地址）
- [ ] 退出登录

---

## 常见问题

**Q：前端连不上后端？**
重新运行 `npm run dev`，确认手机和电脑在同一 WiFi。

**Q：后端启动报数据库连接失败？**
检查 `application-dev.yml` 中的数据库密码是否正确，MySQL 服务是否已启动。

**Q：支付报错 `resource_missing`？**
Stripe payment method ID 填错了，测试环境请使用 `pm_card_visa`。

**Q：注入数据报外键错误？**
先执行 `schema.sql` 确保所有表已创建，再执行 `seed.sql`。
