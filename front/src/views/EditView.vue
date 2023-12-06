<script setup lang="ts">
import { ref } from 'vue';
import axios from 'axios';
import {useRouter} from 'vue-router';

const router = useRouter();

const post = ref({
    id: 0,
    title: "",
    content: "",
});

const props = defineProps({
    postId: {
        type: [Number, String],
        require: true,
    },
});

axios.get(`/my-backend-api/post/${props.postId}`).then((resp) => {
    post.value = resp.data;
})
      

const edit = () => {
    axios.patch(`/my-backend-api/post/${props.postId}`, post.value).then(() => {
        alert('글 수정이 완료되었습니다.');
        router.replace({name: "home"});
})

}


</script>

<template>
    <div class="mt-10">
        <el-input v-model="post.title" type="text" placeholder="제목을 입력해주세요." />
    </div>

    <div>
        <div class="mt-2">
            <el-input v-model="post.content" type="textarea" rows="15" />
        </div>

        <div class="mt-2">
            <el-button type="warning" @click="edit()">글 수정완료</el-button>
        </div>
    </div>
    
</template>

<style>
</style>