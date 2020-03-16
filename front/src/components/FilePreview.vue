<template>
    <div class="root is-relative ">
        <b-loading :active.sync="loading" :is-full-page="false" />
        <img :src="previewSrcChecked" />
    </div>
</template>

<script>
    export default {
        name: "FilePreview",
        props: {
            previewSrc: String,
            loading: Boolean,
        },
        computed: {
            previewSrcChecked() {
                return this.previewSrc || ""
            }
        }
    }
</script>

<style scoped>
    .root {
        display: flex;
        flex-direction: column;
        justify-content: center;
    }
</style>