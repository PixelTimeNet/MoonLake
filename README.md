- **简体中文**
- [English](README-en.md)

<p align="center">
<img src="images/logo.png" alt="MoonLake" />
</p>

<p align="center">
<a href="https://gitter.im/McMoonLakeDev/MoonLake"><img src="https://badges.gitter.im/McMoonLakeDev/MoonLake.svg" alt="Gitter" /></a>
<a href="https://github.com/McMoonLakeDev/MoonLake"><img src="https://badges.frapsoft.com/os/v1/open-source.svg?v=102" alt="OpenSource" /></a>
<a href="http://www.gnu.org/licenses/gpl-3.0"><img src="https://badges.frapsoft.com/os/gpl/gpl.svg?v=102" alt="GPLv3" /></a>
<a href="https://codebeat.co/projects/github-com-mcmoonlakedev-moonlake-v2-0-alpha-kotlin"><img src="https://codebeat.co/badges/71de9e97-982a-4630-a501-07e6c7c35d94" alt="Codebeat" /></a>
</p>

## 介绍

- 此分支将会是未来 `v2.0` 版本的内容. 旧内容请查看 [Override](https://github.com/McMoonLakeDev/MoonLake/tree/override) 分支.

## API

- [x] 反射
- [x] 版本
- [x] 区域
- [x] 服务
- [x] 事件
- [x] 数据包
- [ ] 命令注解
- [x] 高速缓存
- [x] 粒子效果
- [x] NBT 组件
- [x] 铁砧窗口
- [x] 聊天组件
- [x] 软依赖插件
- [x] 物品构建者
- [x] 月色之湖玩家

## 支持

版本: `1.8.x` ~ `1.12.x`

- [x] [Bukkit](https://bukkit.org)
- [x] [Spigot](https://spigotmc.org)
- [x] [PaperSpigot](https://ci.destroystokyo.com/view/All/job/PaperSpigot/)
- [x] [PaperClip](https://ci.destroystokyo.com/view/All/job/PaperSpigot/)

## 安装

您可以在 [GitHub Releases](https://github.com/McMoonLakeDev/MoonLake/releases) 功能中下载已构建的插件文件.

#### 接下来

1. 关闭服务器
2. 将 `MoonLakePlugin.jar` 文件放入 `plugins` 文件夹
3. 运行服务器
4. 在 `plugins/MoonLake/config.yml` 配置文件自定义功能
5. 重载服务器
6. 安装成功

## 自行构建

MoonLake 使用 `Maven` 管理项目依赖关系.

#### 要求

- JDK 8
- Maven 3.3.x
- Git

运行:

```sh
git clone https://github.com/McMoonLakeDev/MoonLake.git
cd MoonLake
mvn clean package
```

您可以在对应模块的 `target` 目录中找到输出的文件.

## 反馈

- 有关错误、问题或讨论，请使用 [GitHub Issues](https://github.com/McMoonLakeDev/MoonLake/issues) 功能.

## 协议

    Copyright (C) 2016-Present The MoonLake (mcmoonlake@hotmail.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
