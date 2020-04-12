import Vue from 'vue';
import Post  from "../components/Post";

const PostPreview = Vue.component('PostPreview', {
    data: function () {
        return {
            post: {
                access: 'private',
                body: '# Header',
                format: 'markdown',
                attachments: [],
            },
        }
    },
    components: {
      Post,
    },
    template: `
        <div class="test-container">
            <post :post="post"/>
        </div>`,

});

export { PostPreview }