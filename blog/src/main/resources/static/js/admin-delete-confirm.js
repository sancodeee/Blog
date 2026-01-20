/**
 * 管理后台删除确认功能
 * 统一处理所有删除操作的二次确认
 */
(function () {
    'use strict';

    // 配置
    const CONFIG = {
        deleteButtonClass: 'delete-btn',
        modalClass: 'delete-confirm-modal'
    };

    // 状态变量
    let currentDeleteUrl = null;
    let isConfirmed = false;

    /**
     * 初始化
     */
    function init() {
        console.log('[删除确认] 开始初始化');

        const $modal = $('.' + CONFIG.modalClass);
        if ($modal.length === 0) {
            console.warn('删除确认模态框未找到');
            return;
        }

        // 初始化 Semantic UI 模态框，完全禁用默认行为
        $modal.modal({
            closable: true,
            allowMultiple: false,
            onDeny: function () {
                console.log('[删除确认] onDeny 触发');
                isConfirmed = false;
                currentDeleteUrl = null;
                return false;  // 阻止默认关闭行为
            },
            onApprove: function () {
                console.log('[删除确认] onApprove 触发，URL:', currentDeleteUrl);
                // 添加额外检查
                if (!isConfirmed || !currentDeleteUrl) {
                    console.log('[删除确认] 未确认或URL为空，取消删除');
                    return false;
                }
                console.log('[删除确认] 执行删除');
                window.location.href = currentDeleteUrl;
                return false;  // 阻止默认关闭行为
            }
        });

        // 绑定删除按钮点击事件
        $(document).on('click', '.' + CONFIG.deleteButtonClass, function (event) {
            event.preventDefault();
            event.stopPropagation();

            const $button = $(this);
            const deleteUrl = $button.data('url');
            const itemName = $button.attr('data-item-name') || '项目';
            const itemTitle = $button.attr('data-item-title') || '';

            if (!deleteUrl) {
                console.error('未找到有效的删除URL');
                return;
            }

            console.log('[删除确认] 点击删除按钮，URL:', deleteUrl);

            // 保存删除URL
            currentDeleteUrl = deleteUrl;
            isConfirmed = false;

            // 设置确认消息
            const message = itemTitle
                ? '确定要删除【' + itemName + '】' + ':' + '【' + itemTitle + '】' + '吗？此操作不可恢复'
                : '确定要删除该【' + itemName + '】吗？此操作不可恢复';

            $modal.find('.delete-message').text(message);
            $modal.find('.delete-title').text('删除' + itemName);

            // 显示模态框
            $modal.modal('show');
            console.log('[删除确认] 模态框已显示');
        });

        // 手动绑定取消按钮（使用更精确的选择器）
        $modal.find('.actions .cancel.button').on('click.cancel', function (e) {
            console.log('[删除确认] 手动取消按钮触发');
            e.preventDefault();
            e.stopPropagation();
            e.stopImmediatePropagation();
            isConfirmed = false;
            currentDeleteUrl = null;
            $modal.modal('hide');
            console.log('[删除确认] 已取消，URL已清除');
            return false;
        });

        // 手动绑定确认删除按钮
        $modal.find('.actions .ok.button').on('click.confirm', function (e) {
            console.log('[删除确认] 手动确认按钮触发');
            e.preventDefault();
            e.stopPropagation();
            e.stopImmediatePropagation();
            isConfirmed = true;
            $modal.modal('hide');  // 先关闭模态框
            // 延迟执行删除，确保模态框完全关闭
            setTimeout(function () {
                if (currentDeleteUrl) {
                    console.log('[删除确认] 执行删除，URL:', currentDeleteUrl);
                    window.location.href = currentDeleteUrl;
                }
            }, 300);
            return false;
        });

        console.log('[删除确认] 初始化完成');
    }

    // 使用 jQuery 确保 DOM 加载完成后初始化
    $(function () {
        init();
    });
})();
