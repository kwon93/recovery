<script setup lang="ts">



import router from '@/router';
import axios from 'axios';
import {ref} from 'vue';

  const posts = ref([]);

    axios.get("/my-backend-api/post?page=1&size=5")
          .then(
                (resp) => {
                        resp.data.forEach((r: any) => posts.value.push(r));
                    }
            );
</script>

<template>
  <ul>
    <li v-for="post in posts" :key="post.id">
        <div class="title">
          <routerLink :to="{name:'read', params: {postId: post.id}}">{{post.title}}</routerLink>
        </div>

        <div class="content">
          {{post.content}}
        </div>

        <div class="d-flex sub">
            <div class="category">개발</div>
            <div class="regDate">2023-12-06</div>
        </div>
    </li>
  </ul>
</template>

<style scoped lang="scss">

  ul{
    list-style: none;
    padding: 0;

    li{
    margin-bottom: 2rem;
      .title{
          a{
            font-size: 1.4rem;
            color: #303030;
            text-decoration: none;
            cursor: pointer;
            }
            
            &:hover{
            text-decoration: underline;
            }
        }
        
          
      }

      .content{
        font-size: 0.9rem;
        margin-top: 10px;
        color: #5d5d5d;
      }

      &:last-child{
        margin-bottom: 0;
      }

      .sub{
        margin-top: 6px;
        font-size: 0.7rem;
        
        
        .regDate{
          color: #656565;
          margin-left: 10px;
        }
      }
  }

  
  


</style>
