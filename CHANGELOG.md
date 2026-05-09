# 更新日志

## 2026-01-21 更新

### 功能优化：用户标识改为学号

**变更内容**：
- ✅ 前端所有"用户ID"文本改为"学号"
- ✅ User实体新增studentId字段（学号字段）
- ✅ 测试数据添加学号：2021001, 2021002, 2021003, 2020001
- ✅ 更新所有表单标签和提示文本

**说明**：
- 页面显示使用"学号"更符合校园场景
- 后端User实体包含studentId字段存储学号
- 测试时仍使用用户ID（1-4）进行查询
- 学号信息已添加到README文档

**涉及文件**：
- `backend/src/main/java/com/campus/entity/User.java` - 添加studentId字段
- `backend/src/main/java/com/campus/config/DataInitializer.java` - 为测试用户添加学号
- `frontend/src/views/ActivityDetail.vue` - 报名表单改为"学号"
- `frontend/src/views/MyRegistrations.vue` - 查询表单改为"学号"
- `README.md` - 更新文档说明

**验证结果**：
- ✅ 所有容器重新构建成功
- ✅ 前端界面正确显示"学号"标签
- ✅ 功能正常运行
