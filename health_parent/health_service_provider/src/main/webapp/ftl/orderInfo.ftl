<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0,user-scalable=no,minimal-ui">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../img/asset-favico.ico">
    <title>编辑信息</title>
    <link rel="stylesheet" href="../css/page-health-orderInfo.css"/>
    <link rel="stylesheet" href="../plugins/elementui/index.css"/>
    <script src="../plugins/jquery/dist/jquery.min.js"></script>
    <script src="../plugins/healthmobile.js"></script>
    <script src="../plugins/datapicker/datePicker.js"></script>
    <script src="../plugins/vue/vue.js"></script>
    <script src="../plugins/vue/axios-0.18.0.js"></script>
    <script src="../plugins/elementui/index.js"></script>
    <script>
        var id = getUrlParam("id");
    </script>
</head>
<body data-spy="scroll" data-target="#myNavbar" data-offset="150">
<div id="app" class="app">
    <!-- 页面头部 -->
    <div class="top-header">
        <span class="f-left"><i class="icon-back" onclick="history.go(-1)"></i></span>
        <span class="center">传智健康</span>
        <span class="f-right"><i class="icon-more"></i></span>
    </div>
    <!-- 页面内容 -->
    <div class="contentBox">
        <div class="card">
            <div class="">
                <img :src="'http://q8pe8ezu7.bkt.clouddn.com/'+${setmeal.img}" width="100%" height="100%"/>
            </div>
            <div class="project-text">
                <h4 class="tit">${setmeal.name}</h4>
                <p class="subtit">${setmeal.remark}</p>
                <p class="keywords">
                            <span>
                            <#if setmeal.sex == '0'>
                                性别不限
                            <#else >
                                <#if setmeal.sex == '1'>
                                    男
                                <#else>
                                    女
                                </#if>
                            </#if>
                            </span>
                    <#--<span>{{setmeal.sex == '0' ? '性别不限' : setmeal.sex == '1' ? '男':'女'}}</span>-->
                    <span>${setmeal.age}</span>
                </p>
            </div>
            <div class="project-know">
                <a href="orderNotice.html" class="link-page">
                    <i class="icon-ask-circle"><span class="path1"></span><span class="path2"></span></i>
                    <span class="word">预约须知</span>
                    <span class="arrow"><i class="icon-rit-arrow"></i></span>
                </a>
            </div>
        </div>
        <div class="form-info">
            <div class="info-title">
                <span class="name">体检人信息</span>
            </div>
            <form class="info-form">
                <div class="input-row">
                    <label>体检人</label>
                    <input v-model="orderInfo.name" type="text" class="input-clear" placeholder="请输入姓名">
                </div>
                <div class="input-row single-radio">
                    <label class="radio-title">性别</label>
                    <div class="radio-list">
                        <div class="radio">
                            <input v-model="orderInfo.sex" id="item1" type="radio" name="sex" value="1" checked>
                            <label for="item1"></label>
                            <span>男</span>
                        </div>
                        <div class="radio">
                            <input v-model="orderInfo.sex" id="item2" type="radio" name="sex" value="2">
                            <label for="item2"></label>
                            <span>女</span>
                        </div>
                    </div>
                </div>
                <div class="input-row">
                    <label>手机号</label>
                    <input v-model="orderInfo.telephone" type="text" class="input-clear" placeholder="请输入手机号">
                    <input style="font-size: x-small;" id="validateCodeButton" @click="sendValidateCode()" type="button"
                           value="发送验证码">
                </div>
                <div class="input-row">
                    <label>验证码</label>
                    <input v-model="orderInfo.validateCode" type="text" class="input-clear" placeholder="请输入验证码">
                </div>
                <div class="input-row">
                    <label>身份证号</label>
                    <input v-model="orderInfo.idCard" type="text" class="input-clear" placeholder="请输入身份证号">
                </div>
                <div class="date">
                    <label>体检日期</label>
                    <i class="icon-date" class="picktime"></i>
                    <input v-model="orderInfo.orderDate" type="text" class="picktime" readonly>
                </div>
            </form>
            <div class="box-button">
                <button @click="submitOrder()" type="button" class="btn order-btn">提交预约</button>
            </div>
        </div>
    </div>
</div>
<script>
    var vue = new Vue({
        el: '#app',
        data: {
            setmeal: {},//套餐信息
            orderInfo: {
                setmealId: id,
                sex: '1'
            }//预约信息
        },
        methods: {
            //发送验证码
            sendValidateCode() {
            },
            //提交预约
            submitOrder() {
            }
        },
        mounted() {
            let id = getUrlParam("id")
            //页面加载就发送请求查询检查套餐信息
            axios.get("/setmeal/findById.do?id=" + id).then((resp) => {
                if (resp.data.flag) {
                    this.setmeal = resp.data.data
                } else {
                    this.$message.error(resp.data.message)
                }
            })
        }
    });
</script>
<script>
    //日期控件
    var calendar = new datePicker();
    calendar.init({
        'trigger': '.picktime', /*按钮选择器，用于触发弹出插件*/
        'type': 'date', /*模式：date日期；datetime日期时间；time时间；ym年月；*/
        'minDate': getSpecifiedDate(new Date(), 1), /*最小日期*/
        'maxDate': getSpecifiedDate(new Date(), 30), /*最大日期*/
        'onSubmit': function () { /*确认时触发事件*/
            //var theSelectData = calendar.value;
        },
        'onClose': function () { /*取消时触发事件*/
        }
    });
</script>
</body>
</html>