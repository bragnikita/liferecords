<template>
  <div class="home">
    <div class="test-wrapper">
    <Editor></Editor>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
import Editor from '@/components/VkPostEditor.vue'

export default {
  name: 'Home',
  components: {
    Editor
  }
}
</script>
<style lang="scss">
  .test-wrapper {
    border: 1px solid gray;
    display: block;
    max-width: 500px;
    margin: auto;
  }
</style>