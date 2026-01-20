/**
 * 博客列表页浏览次数自动更新工具
 */
(function() {
    'use strict';

    // 配置
    const CONFIG = {
        updateInterval: 1000,  // 页面显示后延迟更新时间（毫秒）
        viewsSelector: '.item .eye.icon + span'  // 浏览次数选择器
    };

    /**
     * 从页面中提取所有博客ID
     */
    function extractBlogIds() {
        const ids = [];
        const blogLinks = document.querySelectorAll('a[href*="/blog/"]');

        blogLinks.forEach(function(link) {
            const href = link.getAttribute('href');
            const match = href.match(/\/blog\/(\d+)/);
            if (match && match[1]) {
                const id = match[1];
                if (!ids.includes(id)) {
                    ids.push(id);
                }
            }
        });

        return ids;
    }

    /**
     * 更新页面上的浏览次数
     */
    function updateViews(viewsMap) {
        const blogLinks = document.querySelectorAll('a[href*="/blog/"]');

        blogLinks.forEach(function(link) {
            const href = link.getAttribute('href');
            const match = href.match(/\/blog\/(\d+)/);
            if (match && match[1]) {
                const blogId = match[1];
                if (viewsMap[blogId]) {
                    // 找到对应的浏览次数元素
                    // 从链接向上查找包含浏览次数的容器
                    let container = link.closest('.m-padded-tb');
                    if (!container) {
                        // 如果没有 .m-padded-tb，尝试找父级 generic 元素
                        container = link.closest('div');
                    }

                    if (container) {
                        const viewsElement = container.querySelector(CONFIG.viewsSelector);
                        if (viewsElement) {
                            const oldViews = parseInt(viewsElement.textContent) || 0;
                            const newViews = viewsMap[blogId];

                            // 只在浏览次数增加时更新
                            if (newViews > oldViews) {
                                viewsElement.textContent = newViews;

                                // 添加高亮动画效果
                                viewsElement.style.color = '#e67e22';
                                viewsElement.style.fontWeight = 'bold';
                                setTimeout(function() {
                                    viewsElement.style.color = '';
                                    viewsElement.style.fontWeight = '';
                                }, 2000);
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * 从服务器获取最新的浏览次数
     */
    function fetchLatestViews() {
        const ids = extractBlogIds();

        if (ids.length === 0) {
            return;
        }

        // 发送 AJAX 请求
        fetch('/api/blogs/views?ids=' + ids.join(','))
            .then(function(response) {
                if (!response.ok) {
                    throw new Error('网络响应失败');
                }
                return response.json();
            })
            .then(function(data) {
                updateViews(data);
            })
            .catch(function(error) {
                console.warn('获取浏览次数失败:', error);
            });
    }

    /**
     * 处理 pageshow 事件
     */
    function handlePageShow(event) {
        // 只在从缓存恢复时更新（即用户从详情页返回）
        if (event.persisted) {
            setTimeout(function() {
                fetchLatestViews();
            }, CONFIG.updateInterval);
        }
    }

    /**
     * 处理 visibilitychange 事件（备用方案）
     */
    function handleVisibilityChange() {
        if (document.visibilityState === 'visible') {
            // 检查是否从其他页面返回
            setTimeout(function() {
                fetchLatestViews();
            }, CONFIG.updateInterval);
        }
    }

    /**
     * 初始化
     */
    function init() {
        // 监听 pageshow 事件
        window.addEventListener('pageshow', handlePageShow);

        // 监听 visibilitychange 事件（备用方案）
        document.addEventListener('visibilitychange', handleVisibilityChange);
    }

    // 页面加载完成后初始化
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }

})();
