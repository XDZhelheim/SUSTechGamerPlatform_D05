<template>
    <div id="app">
        <nav>
            <el-menu
            :default-active="activeIndex"
            class="el-menu-demo"
            mode="horizontal"
            @select="handleSelect"
            background-color="#545c64"
            text-color="#fff"
            active-text-color="#ffd04b"
            router>
                <el-menu-item index="/">首页</el-menu-item>
                <el-menu-item index="/allgames">全部游戏</el-menu-item>
                <!-- <el-menu-item index="/shop">游戏商城</el-menu-item> -->
                <el-menu-item v-if="this.$root.loginStatus==true" @click="myGamesVisible=true; getMyGames();">我的游戏</el-menu-item>
                <el-menu-item v-if="this.$root.loginStatus==false" @click="loginFormVisible= true; openSocket();" class="menu-right">登录</el-menu-item>
                <el-menu-item v-else @click="infoVisible = true;" class="menu-right"><el-avatar :size="40" :src="this.$root.userInfo.avatarURL" :key="this.$root.userInfo.avatarURL"></el-avatar></el-menu-item>
                <el-menu-item v-if="this.$root.userInfo.usertype=='A' && this.$root.loginStatus" index="/admin" class="menu-right">管理员</el-menu-item>
                <el-menu-item v-if="this.$root.userInfo.usertype=='D' && this.$root.loginStatus" index="/developer" class="menu-right">开发者</el-menu-item>
                <!-- <el-menu-item index="/hello" class="menu-right">欢迎</el-menu-item> -->
            </el-menu>
        </nav>
        <div id="placehold"></div>
        <router-view />

        <div id="login">
            <el-dialog title="登录" :visible.sync="loginFormVisible" top="30vh" :show-close="false" :lock-scroll="false">
                <el-form :label-position="labelPosition" label-width="100px" size="medium">
                    <el-form-item label="用户名">
                        <el-input v-model="loginForm.username" style="width:var(--itemLength)" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"></el-input>
                    </el-form-item>

                    <el-form-item label="密码">
                        <el-input show-password v-model="loginForm.password" style="width:var(--itemLength)" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"></el-input>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button plain type="primary" style="float: left" @click="loginFormVisible = false; signinFormVisible=true; clearLogin();">注册</el-button>
                    <el-button plain type="warning" @click="clearLogin()">取消</el-button>
                    <el-button plain type="primary" @click="loginFormVisible = false; login();">登录</el-button>
                    <!-- <el-button plain type="primary" @click="loginFormVisible = false; openSocket();">openSocket</el-button>
                    <el-button plain type="primary" @click="loginFormVisible = false; test();">test</el-button> -->
                </div>
            </el-dialog>
        </div>

        <div id="signin">
            <el-dialog title="注册" :visible.sync="signinFormVisible" top="30vh" :show-close="false" :lock-scroll="false">
                <el-form :label-position="labelPosition" label-width="100px" size="medium">
                    <el-form-item label="用户名">
                        <el-input v-model="name" style="width:var(--itemLength)" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"></el-input>
                    </el-form-item>

                    <el-form-item label="密码">
                        <el-input show-password v-model="password" style="width:var(--itemLength)" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"></el-input>
                    </el-form-item>

                    <el-form-item label="确认密码">
                        <el-input show-password v-model="confirmPassword" style="width:var(--itemLength)" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"></el-input>
                    </el-form-item>

                    <el-form-item label="邮箱">
                        <el-input v-model="email" style="width:var(--itemLength)"></el-input>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button plain type="warning" @click="clearRegis()">取消</el-button>
                    <el-button plain type="primary" @click="regis()">注册</el-button>
                    <el-button plain type="primary" style="float: left" @click="loginFormVisible= true; signinFormVisible=false; clearRegis();">登录</el-button>
                </div>
            </el-dialog>
        </div>

        <div id="info">
            <el-dialog title="个人信息" :visible.sync="infoVisible" top="30vh" :lock-scroll="false">
                <el-row>
                    <el-col :span="2"><p></p></el-col>

                    <el-col :span="8">
                        <el-row class="row-avatar" type="flex" justify="center" align="middle">
                            <el-avatar :size="150" :src="this.$root.userInfo.avatarURL" :key="this.$root.userInfo.avatarURL"></el-avatar>
                        </el-row>
                        <el-row type="flex" justify="center">
                            <el-upload
                                class="avatar-uploader"
                                action=""
                                :http-request="submit"
                                :show-file-list="false"
                                :on-success="handleAvatarSuccess"
                                :before-upload="beforeAvatarUpload">
                                <p id="p-upload">上传头像</p>
                            </el-upload>
                        </el-row>
                    </el-col>

                    <el-col :span="2"><p></p></el-col>

                    <el-col :span="12">
                        <el-row>
                            <p class="p-username">用户名:</p>
                            <p class="p-username">{{this.$root.userInfo.username}}</p>
                        </el-row>
                        <el-row>
                            <p class="p-username">账户余额:</p>
                            <p class="p-username">{{this.$root.userInfo.money}}</p>
                            <div>
                                <el-input-number v-model="rechargeMoney" controls-position="right" size="mini" :precision="2" :step="1" :min="0"></el-input-number>&emsp;
                                <el-button size="mini" round plain type="success" @click="recharge();">充值</el-button>
                            </div>
                        </el-row>
                    </el-col>
                </el-row>
                <div slot="footer" class="dialog-footer">
                    <el-button plain type="danger" @click="logout()">登出</el-button>
                </div>
            </el-dialog>
        </div>

        <el-drawer
        title="我的游戏"
        :visible.sync="myGamesVisible"
        direction="ltr"
        size="20%">
            <p v-for="game in myGames" :key="game" class="p-mygames">
                <el-link :underline="false" @click="gotoURL(game.gameURL)">{{game.gametitle}}</el-link>
            </p>
        </el-drawer>

    </div>
</template>


<script>
import ttt from "./views/Shop.vue"
import developer from "./views/Developer"
import w from "./views/gamepages/witcher3"
import al from "./views/AllGames"
import axios from 'axios'
// tt.t()
// import {s} from './views/tt.vue'


export default {
    name: 'app',
    data() {
        return {
            activeIndex: '/',
            loginFormVisible: false,
            signinFormVisible: false,
            infoVisible: false,
            myGamesVisible: false,
            labelPosition: 'right',
            // 注册相关
            name: null,
            password: null,
            confirmPassword: null,
            email: null,
            loginForm: {
                username: '',
                password: '',
            },
            rechargeMoney: 0,
            myGames: [
                // {
                //     gametitle: "The Witcher: Wild Hunt",
                //     gameURL: "/witcher3"
                // },
                // {
                //     gametitle: "原神",
                //     gameURL: "/shop"
                // }
            ]
        }
    },


    methods: {
        clearRegis() {
            this.name=null
            this.password=null
            this.confirmPassword=null
            this.signinFormVisible=false
        },

        clearLogin() {
            this.loginForm.username=''
            this.loginForm.password=''
            this.loginFormVisible=false
        },

        test(){
            alert(al.data().rate)
            al.data().rate = 3
            alert(al.data().rate)
        },


        openSocket(){
            if (typeof WebSocket == "undefined"){
                console.log("不支持webSocket")
            } else {
                console.log("支持webSocket")
                var socketUrl ="http://localhost:8083/"+Math.floor(Math.random() * 10000)
                socketUrl = socketUrl.replace("https", "ws").replace("http", "ws")
                console.log(socketUrl)
                if (this.socket != null) {
                    this.socket.close()
                    this.socket = null
                }
                this.socket = new WebSocket(socketUrl)

                this.socket.onopen = function() {
                    console.log("websocket 打开")
                }
                this.socket.onmessage = function(msg) {
                    // alert(msg.data)
                }
                this.socket.onclose = function() {
                    console.log("关闭")
                }
                this.socket.onerror = function() {
                    console.log("错误")
                }
            }
        },

        login() {
            this.socket.send(
            '{"login":"true","name":"' +
             this.loginForm.username +
            '","password":"' +
             this.loginForm.password +
            '"}')

             this.socket.onmessage = (evt) => {
                var str = evt.data
                var obj = JSON.parse(str)
                if (obj[0].login=="True"){
                    this.$root.loginStatus = true
                    this.$root.userInfo.username = this.loginForm.username
                    this.$root.userInfo.money = parseFloat(obj[0].money)
                    this.$root.userInfo.usertype = obj[0].user_type
                    this.$root.userInfo.ID = obj[0].ID
                    this.$root.userInfo.avatarURL = obj[0].avatarURL
                }else{
                    this.$root.loginStatus = false
                }
            }
        },

        getMyGames(){
            this.socket.send(
            '{"getMyGames":"true","name":"' +
             this.loginForm.username +
            '"}')

             this.socket.onmessage = (evt) => {
                var str = evt.data
                // alert(str)
                var obj = JSON.parse(str)
                this.myGames = obj
            }
        },

        regis() {
            if (this.password!=this.confirmPassword)
                this.$message.error("密码不一致，请重新输入")
            else {
                this.signinFormVisible=false
                this.socket.send(
                '{"register":"true","name":"' +
                this.name +
                '","password":"' +
                this.password +
                '","email":"' +
                this.email +
                '"}')
                this.socket.onmessage = (evt) => {
                    // alert(evt.data)
                }
            }
        },

        handleAvatarSuccess(res, file) {
            // this.$root.userInfo.avatarURL = 
            
        },

        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/jpeg'
            const isLt2M = file.size / 1024 / 1024 < 2
            if (!isJPG) {
            this.$message.error('上传头像图片只能是 JPG 格式!')
            }
            if (!isLt2M) {
            this.$message.error('上传头像图片大小不能超过 2MB!')
            }
            return isJPG && isLt2M
        },

        submit(params) {
            const copyFile = new File([params.file], `${this.$root.userInfo.ID+"-"+Math.random()}`)
            let formData = new FormData()
            formData.append("file", copyFile)
            axios.post('http://localhost:8083/upload/upphoto', formData)
            .then(function (response) {
            // alert(response.data)
            console.log(response)
            })
            .catch(function (error) {
            alert("上传失败")
            console.log(error)
            })
            // this.$root.userInfo.avatarURL='http://localhost:8083/photo/'+this.$root.userInfo.ID
            
            this.$nextTick(() => {
                this.$root.userInfo.avatarURL='http://localhost:8083/photo/' + this.$root.userInfo.ID + '-'+Math.random()
            })
        },

        gotoURL(url) {
            this.$router.push(url)
            this.myGamesVisible=false
        },

        recharge() {
            this.$root.userInfo.money+=this.rechargeMoney
            this.socket.send(
            '{"recharge":"true","name":"' +
             this.$root.userInfo.username +
            '","money":"' +
             this.$root.userInfo.money +
            '"}')
            this.socket.onmessage = (evt) => {
                // alert(evt.data)
            }
        },


        logout() {
            this.$root.loginStatus=false
            this.infoVisible=false
            this.$root.userInfo=[]
            this.$root.witcher3_have=false
            this.$router.push('/')
        },


    },

     mounted() {
            
        }



}
</script>

<style scoped>
    nav {
        position: fixed;
        top: 0%;
        left: 0%;
        width: 100%;
        z-index: 999;
    }

    .el-menu-item.menu-right {
        float: right;
    }

    #placehold {
        height: 61px;
        margin: 0;
        padding: 0;
    }

    .p-username {
        color: aqua;
        font-size: 1.2em;
    }

    #p-upload {
        color: aqua;
    }

    .p-mygames {
        margin-left: 10%;
        font-size: 1.2em; /* 行间距 */
    }

    .row-avatar {
        margin-top: 30px;
    }

</style>

<style>
    #app {
        font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
    }

    :root {
        --itemLength: 90%;
    }

    /* .el-button--primary.is-plain,
    .el-button--default.is-plain {
        background-color: transparent !important;
    } */

    .el-button {
        background-color: transparent !important;
    }

    .el-input--medium .el-input__inner,
    .el-input--mini .el-input__inner,
    .el-input--medium .el-textarea__inner,
    .el-input--medium .el-input__count,
    .el-input-number__decrease,
    .el-input-number__increase {
        background-color: transparent !important;
        border-color: aqua !important;
        color: aqua !important;
    }

    .el-dialog {
        background: rgb(55, 55, 55) !important;
        background-size: cover !important;
        border-radius: 20px !important;
        width: 500px !important;
        border: solid thin lightgoldenrodyellow;
    }

    /* #info .el-dialog {
        height: 400px !important;
    } */

    .el-dialog__title {
        color: white !important;
    }

    .el-form-item__label {
        font-size: 16px !important;
        color: white !important;
    }

    .el-tabs__item {
        color:white !important;
    }

    .el-drawer__header {
        font-size: 20px !important;
        color: aqua !important;
    }

    .el-drawer.ltr {
        background-color: rgb(80, 80, 80) !important;
    }

    .el-link {
        font-size: 16px !important;
        color: aqua !important;
    }

</style>
