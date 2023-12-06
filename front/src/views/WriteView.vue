<script setup lang="ts">
import { ref } from 'vue';
import axios from 'axios';
import {useRouter} from 'vue-router';
import router from '@/router';

const title = ref("");
const content = ref("");

const write = () => {
    axios.post("/my-backend-api/post", {
        title: title.value,
        content: content.value
    })
    .then( ()=> {
        alert('글 작성이 완료되었습니다.')
        router.replace({name:"home"})
    })
}

</script>

<template>
    <div class="mt-10">
        <el-input v-model="title" type="text" placeholder="제목을 입력해주세요." />
    </div>

    <div>
        <div class="mt-2">
            <el-input v-model="content" type="textarea" rows="15" />
        </div>

        <div class="mt-2">
            <div class="d-flex justify-content-end">
                <el-button type="primary" @click="write()">작성 완료</el-button>
            </div>
        </div>
    </div>
    
</template>

<style>
</style>