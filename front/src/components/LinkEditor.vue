<template>
    <div class="_container position-relative">
        <div v-if="!edit && !loading" class="d-flex">
            <div v-if="hasImagePreview"
                 :style="{backgroundImage: `url(${imageUrl})`}"
                 class="thumbed_link_thumb">
            </div>
            <div class="flex-grow-1 description_block p-1">
                <div v-if="title_edit" class="w-100 p-1">
                    <b-input maxlength="100" type="textarea"
                             v-model="title"
                             class="title_inline_editor mb-1"
                    />
                    <b-button size="is-small" @click="title_edit = false">Save</b-button>
                </div>
                <div class="_title"
                     v-if="!title_edit"
                     @click="title_edit = true">
                    {{ title }}
                </div>
                <div class="url">{{ url }}</div>
            </div>
            <div class="_buttons flex-grow-0 p-1">
                <b-button size="is-small" icon-right="external-link-alt"
                          tag="a" :href="url" target="_blank"/>
                <b-button size="is-small" icon-right="pen" @click="edit = true"/>
            </div>
        </div>
        <div v-if="edit && !loading" class="p-2 d-flex align-items-center">
            <b-input v-model="url" icon="link" placeholder="Paste URL here" class="flex-grow-1"></b-input>
            <b-button size="is-small" icon-right="check-circle" class="ml-2" @click="edit = false"/>
            <b-button size="is-small" icon-right="magic" class="ml-1" @click="fetchLink()"/>
        </div>
        <b-loading :active.sync="loading" :is-full-page="false"/>
    </div>
</template>

<script>
    import {getLinkPreview} from 'link-preview-js';

    export default {
        name: "LinkEditor",
        data() {
            return {
                edit: true,
                loading: false,
                url: "",
                imageUrl: null,
                faviconUrl: "",
                title: "",
                title_edit: false,
                description: "",
                cancelTimeout: null,
            }
        },
        watch: {},
        computed: {
            hasImagePreview() {
                return !!this.imageUrl;
            }
        },
        methods: {
            fetchLink() {
                const url = this.url;
                this.loading = true;

                getLinkPreview(url)
                    .then((preview) => {
                        console.log(preview);
                        this.url = preview.url;
                        this.imageUrl = _.head(preview.images) || null;
                        this.faviconUrl = _.head(preview.favicons) || "";
                        this.title = preview.title.substring(0, 100);
                        this.description = preview.description;
                    }).catch(() => {
                    console.log("error");
                    this.url = url;
                    this.imageUrl = null;
                    this.faviconUrl = null;
                    this.title = "";
                    this.description = "";
                }).finally(() => {
                    console.log("finally");
                    this.loading = false;
                    this.edit = false;
                })
            }
        }
    }
</script>

<style scoped lang="scss">
    ._container {
        min-height: 40px;
        border: 1px solid lightgray;
    }

    .thumbed_link_thumb {
        width: 200px;
        min-height: 100px;
        background-color: white;
        background-position: 50% 50%;
        background-size: cover;
        background-repeat: no-repeat;
    }

    .description_block {
        max-width: 70%;

        ._title {
            font-weight: bold;
            overflow: hidden;
        }

        .url {
            font-size: small;
            color: lightgray;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
        }

        ._title, .url {
            display: block;
            width: 100%;
        }
    }

    ._buttons {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
    }
</style>