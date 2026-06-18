# Tlias 综合管理系统

基于 Spring Boot + Vue 3 的全栈员工管理系统，用于企业人事信息化管理。

## 项目结构

```
management-pro/
├── tlias-web-management/     # 后端 - Spring Boot REST API
└── vue-tlias-management/     # 前端 - Vue 3 管理后台
```

---

## 后端 (tlias-web-management)

### 技术栈

| 技术 | 说明 |
|------|------|
| Spring Boot 4.0.3 | Web MVC / AOP / 自动配置 |
| MyBatis 4.0.1 | 数据持久化 |
| MySQL | 关系型数据库 |
| JWT (jjwt 0.9.1) | Token 认证 |
| PageHelper | 分页查询 |
| Aliyun OSS | 文件存储 |
| Lombok | 简化代码 |

### 模块

- **员工管理** — 员工信息 CRUD，支持多条件分页查询、批量删除
- **部门管理** — 部门 CRUD
- **班级管理** — 班级信息管理
- **学生管理** — 学生信息管理，支持多条件分页
- **操作日志** — 基于 AOP 的自动操作日志记录与查询
- **数据报表** — 员工/学生统计报表（供前端 ECharts 展示）
- **登录认证** — JWT Token 认证 + 登录拦截器
- **文件上传** — 对接阿里云 OSS 文件存储
- **全局异常处理** — 统一异常拦截与业务异常处理

### 快速启动

```bash
cd tlias-web-management

# 1. 创建 MySQL 数据库
mysql -u root -p -e "CREATE DATABASE tlias"

# 2. 修改数据库连接信息
# 编辑 src/main/resources/application.yml 中的数据库和OSS配置

# 3. 编译运行
mvn spring-boot:run
```

应用默认启动在 `http://localhost:8080`。

---

## 前端 (vue-tlias-management)

### 技术栈

| 技术 | 说明 |
|------|------|
| Vue 3 | 组合式 API (Composition API) |
| Vite 3 | 构建工具 |
| Element Plus | 组件库 |
| Vue Router 4 | 路由管理 |
| Axios | HTTP 客户端 |
| ECharts + vue-echarts | 数据可视化图表 |

### 页面路由

| 路径 | 页面 | 说明 |
|------|------|------|
| `/login` | 登录 | JWT 认证登录 |
| `/index` | 首页 | 仪表盘概览 |
| `/dept` | 部门管理 | 部门 CRUD |
| `/emp` | 员工管理 | 员工信息管理 |
| `/clazz` | 班级管理 | 班级管理 |
| `/stu` | 学生管理 | 学生信息管理 |
| `/log` | 操作日志 | 查看操作历史 |
| `/report/emp` | 员工报表 | 员工数据统计 |
| `/report/stu` | 学生报表 | 学生数据统计 |

### 快速启动

```bash
cd vue-tlias-management

# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build
```

开发服务器默认启动在 `http://localhost:5173`。

---

## 环境要求

- **JDK** 17+
- **Maven** 3.6+
- **MySQL** 8.0+
- **Node.js** 16+
- **npm** 8+
