<template>
    <el-header>
    <!-- <nav>
      <RouterLink to="/">Home</RouterLink>
      <RouterLink to="/write">글 작성하기</RouterLink>
    </nav> -->
    <el-menu mode="horizontal" router>
      <el-menu-item index="/">Home</el-menu-item>
      <el-menu-item index="/write">글 작성하기</el-menu-item>
    </el-menu>
</el-header>

</template>


<script setup lang="ts">
</script>

<style scoped>
  .header{
    padding: 0;
    height: 60px;
  }
</style>
