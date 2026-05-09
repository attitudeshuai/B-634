# 校园活动报名与签到系统

简洁、现代、大气的校园活动管理系统，支持活动发布、在线报名、现场签到等核心功能。

## 🛠 技术栈

- **Frontend**: Vue 3 + Vite + WindiCSS + Vue Router + Axios
- **Backend**: SpringBoot 3.2.1 + Spring Data JPA + MySQL
- **Database**: MySQL 8.0
- **容器化**: Docker + Docker Compose

## 🎨 设计特色

- **主色调**: 深红色 RGB(150, 30, 25)
- **风格**: 简洁、现代、大气，具有学术感
- **布局**: 响应式设计，支持PC和移动端
- **交互**: 卡片式布局，流畅的动画效果

## 🚀 启动指南

### 前置要求
- Docker Desktop 已安装并运行

### 一键启动

```bash
docker compose up --build
```

等待所有容器启动完成（首次启动可能needs几分钟）...

## 🔗 服务地址

- **前端页面**: http://localhost:3000
- **后端API**: http://localhost:8080/api/
- **数据库**: localhost:3306
  - 用户名: `root`
  - 密码: `rjxy123`
  - 数据库: `campus_activity`

## 📱 功能模块

### 1. 活动列表
- 查看所有校园活动
- 按状态筛选（全部/即将开始/进行中/已结束）
- 卡片式展示，包含活动信息和报名状态

### 2. 活动详情
- 查看活动完整信息
- 查看报名进度
- 在线报名功能

### 3. 我的报名
- 查看个人报名记录
- 查看签到状态
- 取消报名

### 4. 签到功能
- 通过报名ID进行签到
- 签到成功提示
- 防止重复签到

### 5. 管理后台
- 查看所有活动
- 创建活动
- 删除活动
- 查看活动报名列表

## 🧪 测试账号

系统已预置测试数据：

- **测试用户** (使用用户ID进行操作):
  - 学号: 1 - 张三 ( zhangsan@campus.edu)
  - 学号: 2 - 李四 (lisi@campus.edu)
  - 学号: 3 - 王五 ( wangwu@campus.edu)
  - 学号: 4 - admin ( admin@campus.edu)


- **预置活动**: 6个活动（5个即将开始，1个已结束）

- **预置报名**: 已有部分测试报名数据

## 📖 使用流程

1. 访问 http://localhost:3000
2. 浏览活动列表
3. 点击活动查看详情
4. 输入学号（1-4，页面显示为"学号"）进行报名
5. 在"我的报名"页面输入学号查看报名记录
6. 使用报名ID在"签到"页面进行签到
7. 管理员可在"管理后台"创建新活动

## 🛑 停止服务

```bash
docker compose down
```

## 🗑️ 清理数据

如需清除所有数据并重新开始：

```bash
docker compose down -v
docker compose up --build
```

## 📝 开发说明

- 后端使用JPA自动建表，无需手动创建数据库表
- 首次启动时会自动初始化演示数据
- 前端通过Nginx反向代理访问后端API
- 所有服务使用Docker网络互联，无需配置IP

## 🏗️ 项目结构

```
.
├── backend/              # SpringBoot后端
│   ├── src/
│   │   └── main/
│   │       ├── java/com/campus/
│   │       │   ├── entity/      # 实体类
│   │       │   ├── repository/  # 数据访问层
│   │       │   ├── service/     # 业务逻辑层
│   │       │   ├── controller/  # 控制器层
│   │       │   └── config/      # 配置类
│   │       └── resources/
│   ├── pom.xml
│   └── Dockerfile
├── frontend/             # Vue前端
│   ├── src/
│   │   ├── api/         # API封装
│   │   ├── components/  # 组件
│   │   ├── router/      # 路由
│   │   ├── views/       # 页面
│   │   ├── App.vue
│   │   └── main.js
│   ├── package.json
│   ├── nginx.conf
│   └── Dockerfile
└── docker-compose.yml    # Docker编排
```