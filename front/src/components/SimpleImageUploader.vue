<template>
    <b-field class="upload-zone">
        <b-upload v-model="image">
            <a class="button app-btn">
                <b-icon icon="upload"/>
            </a>
        </b-upload>
    </b-field>
</template>

<script>
    export default {
        name: "SimpleImageUploader",
        props: {},
        data: function () {
            return {
                image: null
            }
        },
        watch: {
            image(newImage, oldImage) {
                //TODO
                const url = "https://picsum.photos/seed/picsum/1024/680";
                const id = `20120112_${newImage.name}.jpg`;
                this.$emit("uploaded", {
                    url, id
                })
            }
        }
    }
</script>

<style scoped>
    .upload-zone {

    }

    .app-btn {
        border: 1px solid gray;
        width: 100px;
        height: 100px;
        overflow: hidden;
        margin-right: 5px;
        margin-bottom: 5px;
        word-break: break-word;
        border-radius: 5px;
    }
</style>