<script>
import { defineProps, onMounted } from 'vue';
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

export default {
  props: {
    postId: {
      type: [Number, String],
      required: true,
    },
  },
  setup(props) {
    const router = useRouter();

    const post = ref({
      id: 0,
      title: "",
      content: ""
    });

    onMounted(async () => {
      try {
        const response = await axios.get(`/my-backend-api/post/${props.postId}`);
        post.value = response.data;
      } catch (error) {
        console.error('데이터를 불러오는 중 에러 발생:', error);
      }
    });

    const moveToEdit = () => {
      router.push({ name: "edit" , params: {postId: props.postId} });
    };

    return {
      post,
      moveToEdit
    };
  },
};
</script>

<template>
  <div v-if="post">
    <el-row>
        <el-col>
            <h2 class="title">{{ post.title }}</h2>
            <div class="sub d-flex">
                <div class="category">개발</div>
                <div class="regDate">2023-12-06</div>
            </div>
        </el-col>

    </el-row>

    <el-row>
        <el-col>
            <div class="content">{{ post.content }}</div>
        </el-col>
    </el-row>

    <el-row>
        <el-col>
            <div class="d-flex justify-content-end">
                <el-button type="warning" @click="moveToEdit">수정</el-button>
            </div>
        </el-col>
    </el-row>
  </div>

  
  
</template>

<style scoped lang="scss">
    .title{
        font-size: 1.6rem;
        font-weight: 600;
        color: #383838;
    }

    .content{
        font-size: 0.95rem;
        margin-top: 8px;
        margin-bottom: 20px;
        color: #5e5e5e;
        white-space: break-spaces;
        line-height: 1.5;
    }


    .sub{
        margin-top: 6px;
        font-size: 0.7rem;
        
        
        .regDate{
          color: #656565;
          margin-left: 10px;
        }
      }
</style>
