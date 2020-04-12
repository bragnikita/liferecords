<template>
    <div class="post">
        <div class="status">{{ status }}</div>
        <div :class="['body', post.format]" v-html="body"></div>
        <div class="attachments" v-if="attachments.length > 0">
            <div class="attachment" v-for="att in attachments" :key="att">
                <img :src="att" :alt="'image ' + att"/>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Post",
        props: {
            post: {
                type: Object,
                required: true,
            }
        },
        data: function () {
            const post = this.post;
            const attachments = post.attachments ?
                post.attachments.map(expandResourceUrl) : [];

            return {
                status: post.access,
                body: translateBody(post.format, post.body),
                attachments,
            }
        }
    }

    const translateBody = (format, text) => {
        // TODO
        return text;
    };

    const expandResourceUrl = (link) => {
        // TODO
        return "/resources/" + link;
    };
</script>

<style scoped lang="scss">
    .post {
        min-width: 300px;
    }
    .status {
        font-size: x-small;
        text-align: right;
    }
    .body {
        padding: 5px;
        border-bottom: 1px solid gainsboro;
        &.markdown {
            white-space: pre-line;
        }
    }
    .attachments {
        padding: 5px;
        border-bottom: 1px solid gainsboro;
    }
</style>