<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<!--
This is a starter template page. Use this page to start your new project from
scratch. This page gets rid of all links and provides the needed markup only.
-->
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>AdminLTE 2 | Starter</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <%@ include file="/WEB-INF/views/admin/include/plugin1.jsp" %>
  
   <script>
      let msg = '${msg}';
      if(msg != '') {
        alert(msg);
      }

    </script>
</head>
<!--
BODY TAG OPTIONS:
=================
Apply one or more of the following classes to get the
desired effect
|---------------------------------------------------------|
| SKINS         | skin-blue                               |
|               | skin-black                              |
|               | skin-purple                             |
|               | skin-yellow                             |
|               | skin-red                                |
|               | skin-green                              |
|---------------------------------------------------------|
|LAYOUT OPTIONS | fixed                                   |
|               | layout-boxed                            |
|               | layout-top-nav                          |
|               | sidebar-collapse                        |
|               | sidebar-mini                            |
|---------------------------------------------------------|
-->
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

  <!-- Main Header -->
	<%@ include file="/WEB-INF/views/admin/include/header.jsp" %>
  <!-- Left side column. contains the logo and sidebar -->
  <%@ include file="/WEB-INF/views/admin/include/nav.jsp" %>

  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
      <h1>
        Page Header
        <small>Optional description</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-dashboard"></i> Level</a></li>
        <li class="active">Here</li>
      </ol>
    </section>

    <!-- Main content -->
    <section class="content container-fluid">

      <!--------------------------
        | Your Page Content Here |
        -------------------------->

	<div class="row">
     	<div class="col-md-12">
     		<div class="box box-primary">
     			<form id="productForm" action="productModify" method="post" enctype="multipart/form-data">
     			<div class="box-header">
     				MODIFY PRODUCT
     			</div>
     			<div class="box-body">	
					  <div class="form-group row">
					    <!-- 페이지및검색정보(Criteria클래스의 필드)? 원래상태의 리스트에서 필요한 정보 -->
					    <input type="hidden" name="pageNum" value="${cri.pageNum }">
					    <input type="hidden" name="amount" value="${cri.amount }">
					    <input type="hidden" name="type" value="${cri.type }">
					    <input type="hidden" name="keyword" value="${cri.keyword }">
					    
					    <label for="pdt_name" class="col-sm-2 col-form-label">카테고리</label>
					    <div class="col-sm-3">
					    	<select name="cate_code_prt" id="firstCategory" class="form-control">
					    		<option value="">1차카테고리 선택</option>
					    		<c:forEach items="${cateList }" var="categoryVO">
					    			<option value="${categoryVO.cate_code }" ${ categoryVO.cate_code == cate_code_prt ? 'selected':'' }>${categoryVO.cate_name }</option>
					    		</c:forEach>
					    	</select>
					    </div>
						<div class="col-sm-3">					    	
					    	<select name="cate_code" id="secondCategory" class="form-control">
					    		<option>2차카테고리 선택</option>
					    		<c:forEach items="${subCateList }" var="subCategoryVO">
					    			<option value="${subCategoryVO.cate_code }" ${ subCategoryVO.cate_code == cate_code ? 'selected':'' }>${subCategoryVO.cate_name }</option>
					    		</c:forEach>
					    	</select>
					    </div>
					  </div>
					  <div class="form-group row">
					    <label for="pdt_name" class="col-sm-2 col-form-label">상품명</label>
					    <div class="col-sm-4">
					      <!-- 상품코드 -->
					      <input type="hidden" id="pdt_num" name="pdt_num" value="${productVO.pdt_num }">
					      <input type="text" class="form-control" id="pdt_name" name="pdt_name" value="${productVO.pdt_name }">
					    </div>
					    <label for="pdt_price" class="col-sm-2 col-form-label">상품가격</label>
					    <div class="col-sm-4">
					      <input type="text" class="form-control" id="pdt_price" name="pdt_price" value="${productVO.pdt_price }">
					    </div>
					  </div>
					  <div class="form-group row">
					    <label for="pdt_discount" class="col-sm-2 col-form-label">할인율</label>
					    <div class="col-sm-4">
					      <input type="text" class="form-control" id="pdt_discount" name="pdt_discount" value="${productVO.pdt_discount }">
					    </div>
					    <label for="pdt_company" class="col-sm-2 col-form-label">제조사</label>
					    <div class="col-sm-4">
					      <input type="text" class="form-control" id="pdt_company" name="pdt_company" value="${productVO.pdt_company }">
					    </div>
					  </div>
					  <div class="form-group row">
					    <label for="pdt_img" class="col-sm-2 col-form-label">상품이미지</label>
					    <div class="col-sm-10"> <!-- name="pdt_img" 존재안함.  스프링에서는 Null 이 됨.-->
					      <input type="file" class="form-control" id="uploadFile" name="uploadFile">
					      
					      <!--  날짜폴더, 상품이미지 파일명 -->
					      <input type="hidden" id="pdt_img_folder" name="pdt_img_folder" value="${productVO.pdt_img_folder }">
					      <input type="hidden" id="pdt_img" name="pdt_img" value="${productVO.pdt_img }">
					    </div>
					  </div>
					  <!-- 상품이미지 보기 -->
					  <div class="form-group row">
					    <label for="pdt_img" class="col-sm-2 col-form-label">현재 이미지</label>
					    <div class="col-sm-4">
					      <img src="/admin/product/displayFile?folderName=${productVO.pdt_img_folder }&fileName=${productVO.pdt_img }" id="cur_img" style="width: 200px;height: 200px;">
					    </div>
					    <label for="pdt_img" class="col-sm-2 col-form-label">변경 이미지</label>
					    <div class="col-sm-4">
					      <img id="change_img" style="width: 200px;height: 200px;">
					    </div>
					  </div>
					  
					  
					  
					  <div class="form-group row">
					    <label for="pdt_detail" class="col-sm-2 col-form-label">상품설명</label>
					    <div class="col-sm-10">
					      <textarea class="form-control" id="pdt_detail" name="pdt_detail" rows="3">${productVO.pdt_detail }</textarea>
					    </div>
					  </div>
					  <div class="form-group row">
					    <label for="pdt_amount" class="col-sm-2 col-form-label">수량</label>
					    <div class="col-sm-4">
					      <input type="text" class="form-control" id="pdt_amount" name="pdt_amount" value="${productVO.pdt_amount }">
					    </div>
					    <label for="pdt_buy" class="col-sm-2 col-form-label">판매여부</label>
					    <div class="col-sm-4">
					      <select id="pdt_buy" name="pdt_buy">
					      	<option value="Y" ${ productVO.pdt_buy == 'Y' ? 'selected':'' }>판매가능</option>
					      	<option value="N" ${ productVO.pdt_buy == 'N' ? 'selected':'' }>판매불가능</option>
					      </select>
					    </div>
					  </div>	  
				 
     			</div>
     			<div class="box-footer">
     				<div class="form-group">
     					<ul class="uploadedList"></ul>
     				</div>    				
     				<div class="form-group row">
						  <div class="col-sm-12 text-center">
						  	<button type="submit" class="btn btn-dark" id="btnProduct">상품수정</button>
						  </div>			
					</div>
     			</div>
     			</form>
     		</div>
     	</div>
     </div>


    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->

  <!-- Main Footer -->
  <%@ include file="/WEB-INF/views/admin/include/footer.jsp" %>

  <!-- Control Sidebar -->
  <aside class="control-sidebar control-sidebar-dark">
    <!-- Create the tabs -->
    <ul class="nav nav-tabs nav-justified control-sidebar-tabs">
      <li class="active"><a href="#control-sidebar-home-tab" data-toggle="tab"><i class="fa fa-home"></i></a></li>
      <li><a href="#control-sidebar-settings-tab" data-toggle="tab"><i class="fa fa-gears"></i></a></li>
    </ul>
    <!-- Tab panes -->
    <div class="tab-content">
      <!-- Home tab content -->
      <div class="tab-pane active" id="control-sidebar-home-tab">
        <h3 class="control-sidebar-heading">Recent Activity</h3>
        <ul class="control-sidebar-menu">
          <li>
            <a href="javascript:;">
              <i class="menu-icon fa fa-birthday-cake bg-red"></i>

              <div class="menu-info">
                <h4 class="control-sidebar-subheading">Langdon's Birthday</h4>

                <p>Will be 23 on April 24th</p>
              </div>
            </a>
          </li>
        </ul>
        <!-- /.control-sidebar-menu -->

        <h3 class="control-sidebar-heading">Tasks Progress</h3>
        <ul class="control-sidebar-menu">
          <li>
            <a href="javascript:;">
              <h4 class="control-sidebar-subheading">
                Custom Template Design
                <span class="pull-right-container">
                    <span class="label label-danger pull-right">70%</span>
                  </span>
              </h4>

              <div class="progress progress-xxs">
                <div class="progress-bar progress-bar-danger" style="width: 70%"></div>
              </div>
            </a>
          </li>
        </ul>
        <!-- /.control-sidebar-menu -->

      </div>
      <!-- /.tab-pane -->
      <!-- Stats tab content -->
      <div class="tab-pane" id="control-sidebar-stats-tab">Stats Tab Content</div>
      <!-- /.tab-pane -->
      <!-- Settings tab content -->
      <div class="tab-pane" id="control-sidebar-settings-tab">
        <form method="post">
          <h3 class="control-sidebar-heading">General Settings</h3>

          <div class="form-group">
            <label class="control-sidebar-subheading">
              Report panel usage
              <input type="checkbox" class="pull-right" checked>
            </label>

            <p>
              Some information about this general settings option
            </p>
          </div>
          <!-- /.form-group -->
        </form>
      </div>
      <!-- /.tab-pane -->
    </div>
  </aside>
  <!-- /.control-sidebar -->
  <!-- Add the sidebar's background. This div must be placed
  immediately after the control sidebar -->
  <div class="control-sidebar-bg"></div>
</div>
<!-- ./wrapper -->

<!-- REQUIRED JS SCRIPTS -->
<%@ include file="/WEB-INF/views/admin/include/plugin2.jsp" %>
<!-- CKEditor 파일참조-->
<script src="/bower_components/ckeditor/ckeditor.js"></script>
<script>
  $(document).ready(function() {
    
    // ckeditor 환경설정(자바스크립트 Object 문법. 적용 태그 : textarea, name을 replace함수에 일치시켜줘야 함)
    var ckeditor_config = {
			resize_enabled : false,
			enterMode : CKEDITOR.ENTER_BR,
			shiftEnterMode : CKEDITOR.ENTER_P,
			toolbarCanCollapse : true,
			removePlugins : "elementspath", 
			//업로드 탭기능추가 속성. CKEditor에서 파일 업로드해서 서버로 전송 클릭하면, 이 주소가 동작한다.
			filebrowserUploadUrl: '/admin/product/imageUpload' 
    }

    CKEDITOR.replace("pdt_detail", ckeditor_config);
    
    console.log("버전: " + CKEDITOR.version);
    
    
   

  });

</script>
</body>
</html>