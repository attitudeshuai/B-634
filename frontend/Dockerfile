# 构建阶段 - 使用淘宝镜像加速
FROM node:18-alpine AS builder

WORKDIR /app

# 配置npm使用淘宝镜像
RUN npm config set registry https://registry.npmmirror.com

# 复制package文件
COPY package.json ./

# 安装依赖
RUN npm install

# 复制源代码
COPY . .

# 构建生产版本
RUN npm run build

# 生产阶段 - 使用Nginx提供静态文件服务
FROM nginx:alpine

# 复制构建产物
COPY --from=builder /app/dist /usr/share/nginx/html

# 复制Nginx配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 暴露端口
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
