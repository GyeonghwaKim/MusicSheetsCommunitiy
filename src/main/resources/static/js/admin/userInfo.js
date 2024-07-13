$(document).ready(function () {
    window.loadUserProfile = function (userId) {
        $.ajax({
            url: '/admin/userProfile/' + userId,
            type: 'GET',
            success: function (data) {
                $("#userProfile").attr('src',data.storedImgName);
                $("#username").val(data.username);
                $("#userEmail").val(data.email);
            },
            error: function (error) {
                alert("사용자를 찾을 수 없습니다")
                window.location.reload()
            }
        });
    };

    window.loadUserPost = function (userId, postPageNumber) {
        $.ajax({
            url: '/admin/userPost/' + userId,
            type: 'GET',
            data: {
                page: postPageNumber
            },
            success: function (data) {
                var postList = data.pagedPostList.content;
                var postHtml = '';
                $.each(postList, function (index, post) {

                    var formattedDate = formatDateToLocal(post.createAt);

                    postHtml += `<tr>
                                            <th scope="row"> ${index + 1}</th>
                                            <td><a href="/post/detail/${post.id}">${post.title}</a></td>
                                            <td>${formattedDate}</td>
                                            <td><button class="btn btn-outline-dark deletePostBtn" data-id=${post.id}>Delete</button></td>
                                        </tr>`;
                });


                $('#postList').html(postHtml);

                var pageable = data.pagedPostList.pageable;
                var totalPages = data.pagedPostList.totalPages;
                var currentPage = pageable.pageNumber;
                var postPaginationHtml = '';

                if (totalPages > 0) {
                    postPaginationHtml += `
                                <ul class="pagination pagination-sm">
                                    <li class="page-item ${currentPage === 0 ? 'disabled' : ''}">
                                        <a class="page-link postPage" href="#" aria-label="Previous" data-page="${currentPage - 1}" data-user-id="${userId}">
                                            <span aria-hidden="true">&laquo;</span>
                                        </a>
                                    </li>`;

                    let startPage = Math.max(0, currentPage - 1);
                    let endPage = Math.min(totalPages, startPage + 3);

                    for (let i = startPage; i < endPage; i++) {
                        postPaginationHtml += `
                                    <li class="page-item ${currentPage === i ? 'active' : ''}">
                                        <a class="page-link postPage" href="#" data-page="${i}" data-user-id="${userId}">${i + 1}</a>
                                    </li>`;
                    }

                    postPaginationHtml += `
                                <li class="page-item ${currentPage === totalPages - 1 ? 'disabled' : ''}">
                                    <a class="page-link postPage" href="#" aria-label="Next" data-page="${currentPage + 1}" data-user-id="${userId}">
                                        <span aria-hidden="true">&raquo;</span>
                                    </a>
                                </li>
                            </ul>`;
                }

                $('#postPagination').html(postPaginationHtml);
            },
            error: function (error) {
                alert("사용자를 찾을 수 없습니다")
                window.location.reload()
            }
        });
    };

    window.loadUserComment = function (userId, commentPageNumber) {
        $.ajax({
            url: '/admin/userComment/' + userId,
            type: 'GET',
            data: {
                page: commentPageNumber
            },
            success: function (data) {
                var commentList = data.pagedCommentList.content;
                var commentHtml = '';

                $.each(commentList, function (index, comment) {


                    var formattedDate = formatDateToLocal(comment.createAt);


                    commentHtml += `<tr>
                                                <th scope="row">${index + 1}</th>
                                                <td>${comment.content}</td>
                                                <td><a href="/post/detail/${comment.postId}">${comment.postTitle}</a></td>
                                                <td>${formattedDate}</td>
                                                <td><button class="btn btn-outline-dark deleteCommentBtn" data-id="${comment.id}">Delete</button></td>;
                                            </tr>`
                });


                $('#commentList').html(commentHtml);

                var pageable = data.pagedCommentList.pageable;
                var totalPages = data.pagedCommentList.totalPages;
                var currentPage = pageable.pageNumber;
                var commentPaginationHtml = '';

                if (totalPages > 0) {
                    commentPaginationHtml += `
                                <ul class="pagination pagination-sm">
                                    <li class="page-item ${currentPage === 0 ? 'disabled' : ''}">
                                        <a class="page-link commentPage" href="#" aria-label="Previous" data-page="${currentPage - 1}" data-user-id="${userId}">
                                            <span aria-hidden="true">&laquo;</span>
                                        </a>
                                    </li>`;

                    let startPage = Math.max(0, currentPage - 1);
                    let endPage = Math.min(totalPages, startPage + 3);

                    for (let i = startPage; i < endPage; i++) {
                        commentPaginationHtml += `
                                    <li class="page-item ${currentPage === i ? 'active' : ''}">
                                        <a class="page-link commentPage" href="#" data-page="${i}" data-user-id="${userId}">${i + 1}</a>
                                    </li>`;
                    }

                    commentPaginationHtml += `
                                <li class="page-item ${currentPage === totalPages - 1 ? 'disabled' : ''}">
                                    <a class="page-link commentPage" href="#" aria-label="Next" data-page="${currentPage + 1}" data-user-id="${userId}">
                                        <span aria-hidden="true">&raquo;</span>
                                    </a>
                                </li>
                            </ul>`;
                }

                $('#commentPagination').html(commentPaginationHtml);
            },
            error: function (error) {
                alert("사용자를 찾을 수 없습니다")
                window.location.reload()
            }
        });
    };


    function formatDateToLocal(dateString) {
        var date = new Date(dateString);
        var year = date.getFullYear();
        var month = ('0' + (date.getMonth() + 1)).slice(-2);
        var day = ('0' + date.getDate()).slice(-2);
        var hours = ('0' + date.getHours()).slice(-2);
        var minutes = ('0' + date.getMinutes()).slice(-2);

        return `${year}-${month}-${day} ${hours}:${minutes}`;
    }

    $('#userInfoModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget);
        var userId = button.data('user-id');
        $('#userInfoModal').attr('data-user-id', userId);

        loadUserProfile(userId);
        loadUserPost(userId, 0);
        loadUserComment(userId, 0);
    });

    $(document).on('click', '.pagination a.postPage', function (event) {
        event.preventDefault();
        var userId = $(this).data('user-id');
        var page = $(this).data('page');
        loadUserPost(userId, page);
    });

    $(document).on('click', '.pagination a.commentPage', function (event) {
        event.preventDefault();
        var userId = $(this).data('user-id');
        var page = $(this).data('page');
        loadUserComment(userId, page);
    });

    $(document).on('click', '.deletePostBtn', function () {
        var postId = $(this).data('id');
        var userId = $('#userInfoModal').attr('data-user-id');
        var currentPostPage = $('.pagination .active a.postPage').data('page');
        var currentCommentPage = $('.pagination .active a.commentPage').data('page');

        if (confirm('정말로 이 게시물을 삭제하시겠습니까?')) {
            var csrfToken = $('meta[name="_csrf"]').attr('content');
            var csrfHeader = $('meta[name="_csrf_header"]').attr('content');

            $.ajax({
                url: '/admin/post/delete/' + postId,
                type: 'DELETE',
                beforeSend: function (xhr) {

                    xhr.setRequestHeader(csrfHeader, csrfToken);
                },
                success: function (result) {
                    loadUserPost(userId, currentPostPage);
                    loadUserComment(userId, currentCommentPage);

                    alert('게시물이 삭제되었습니다.');
                },
                error: function (xhr, status, error) {
                    alert('삭제에 실패했습니다.');
                }
            });
        }
    });

    $(document).on('click', '.deleteCommentBtn', function () {
        var commentId = $(this).data('id');
        var userId = $('#userInfoModal').attr('data-user-id');
        var currentPage = $('.pagination .active a.commentPage').data('page');


        if (confirm('정말로 이 댓글을 삭제하시겠습니까?')) {
            var csrfToken = $('meta[name="_csrf"]').attr('content');
            var csrfHeader = $('meta[name="_csrf_header"]').attr('content');

            $.ajax({
                url: '/admin/comment/delete/' + commentId,
                type: 'DELETE',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                },
                success: function (result) {
                    loadUserComment(userId, currentPage);
                    alert('댓글이 삭제되었습니다.');
                },
                error: function (xhr, status, error) {
                    alert('삭제에 실패했습니다.');
                }
            });
        }
    });
});