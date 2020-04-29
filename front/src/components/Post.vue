<template>
    <div class="post">
        <div class="status">{{ status }}</div>
        <div :class="['body', post.body_format]" v-html="body"></div>
        <div class="attachments" v-if="hasAttachments">
            <div v-for="att in attachments" v-bind:key="att.id" class="attachment-wrapper">
                <div v-if="att.type==='image'" class="att_image position-relative">
                    <img :src="att.url"/>
                </div>
                <a v-if="att.type==='link'" class="att_link d-block" :href="att.href">
                    <span>{{ att.title }}</span>
                </a>
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
                post.attachments.map(buildCommonAttachment).filter((v) => v !== null) : [];

            return {
                status: post.access,
                body: translateBody(post.format, post.body_content),
                attachments,
            }
        },
        computed: {
            hasAttachments() {
                return this.attachments.length > 0;
            },
        },
    }

    const translateBody = (format, text) => {
        // TODO
        return text;
    };

    const buildCommonAttachment = (attachment, index) => {
        if (attachment.type === 'image') {
            return {
                id: index,
                type: "image",
                url: attachment.url,
            }
        }
        if (attachment.type === 'link') {
            return {
                id: index,
                type: 'link',
                href: attachment.url,
                title: attachment.title,
            }
        }
        return null;
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

        &.md {
            white-space: pre-line;
        }

        &.plain {
            text-align: left;
        }
    }

    .attachments {
        padding: 5px;
        border-bottom: 1px solid gainsboro;

    }
    .attachment-wrapper {
        margin-bottom: 10px;
    }

    .att_image {
        max-height: 350px;
        overflow: hidden;

        img {
            height: auto;
            max-width: 100%;
        }
    }

    .att_link {
        border: 1px solid lightgray;
        padding: 5px;
        text-align: left;

    }

</style>