<template>
    <div class="wrapper p-2">
        <div>{{ publishDay }}</div>
        <div class="access py-1 d-flex justify-content-between align-items-center">
            <b-field>
                <b-radio-button v-model="access" native-value="private" type="is-success">
                    <b-icon icon="lock"/>
                    Private
                </b-radio-button>
                <b-radio-button v-model="access" native-value="public" type="is-danger">
                    <b-icon icon="globe"/>
                    Public
                </b-radio-button>
            </b-field>
            <b-switch v-model="useMarkdown" style="margin-bottom: 0.75rem">
                Markdown
            </b-switch>
        </div>
        <div class="body mt-3">
            <b-field label="Text">
                <b-input rows="10" type="textarea" v-model="bodyText"></b-input>
            </b-field>
        </div>
        <div class="attachments mt-3">
            <div class="images">
                <div v-for="cell of imagesGrid" v-bind:key="cell.id" :class="{'cell': true, 'btn': !cell.url}">
                    <div v-if="cell.url" class="position-relative if-hovered">
                        <div class="fill-container if-hovered-overlay darken justify-content-end p-2">
                            <span @click="removeImage(cell.id)">
                                <b-icon icon="times-circle"/>
                            </span>
                        </div>
                        <img :src="cell.url">
                    </div>
                    <SimpleImageUploader @uploaded="addImage" v-else/>
                </div>
            </div>
            <div class="links">
                <a v-for="link of this.links" v-bind:key="link.url" :href="link.url" class="d-flex">
                    <div>{{link.title}}</div>
                    <div>{{link.url}}</div>
                </a>
                <LinkEditor/>
            </div>
        </div>
        <div class="p-3 d-flex justify-content-center">
            <b-button type="is-danger">
                Cancel
            </b-button>
            <b-button type="is-success" class="ml-3">
                Save
            </b-button>
        </div>
    </div>
</template>

<script>

    import moment from 'moment';
    import _ from 'lodash';
    import ImageUploader from "./ImageUploader";
    import SimpleImageUploader from "./SimpleImageUploader";
    import LinkEditor from "./LinkEditor";

    export default {
        components: {
            LinkEditor,
            SimpleImageUploader,
            uploader: ImageUploader,
        },
        props: {
            day: {
                type: Date,
                default: () => new Date(),
            }
        },
        data() {
            return {
                access: "private",
                bodyText: "",
                // { previewUrl: <>, id: <YYYYMMDD_filename> }
                images: [],
                links: [],
                useMarkdown: true,
            }
        },
        methods: {
            formatDate(date) {
                return moment(date).format('YYYY-MM-DD (ddd)');
            },
            addImage(image) {
                this.images.push(image);
            },
            removeImage(id) {
                console.log("delete", id);
                this.images = this.images.filter((i) => i.id !== id);
                console.log(this.images)
            }
        },
        computed: {
            publishDay() {
                return moment(this.day).format("MMM DD (ddd)")
            },
            imagesGrid() {
                return this.images.concat({id: '___add'});
            },
            linksList() {
                return this.links;
            },
        }
    }


    // function formatDate(date) {
    //     return moment(date).format('YYYY-MM-DD');
    // }
    // function parseDate(dateStr) {
    //     return moment(dateStr, 'YYYY-MM-DD').toDate();
    // }
</script>

<style scoped lang="scss">
    .tag {
        cursor: pointer;
    }

    .images {
        display: flex;
        flex-wrap: wrap;

        .cell {
            width: 50%;
            padding: 10px;

        }

        .cell.btn {
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .cell:first-child {
            display: flex;
            justify-content: left;
        }
    }

    .if-hovered-overlay {
        display: none;
    }

    .if-hovered:hover > .if-hovered-overlay {
        display: flex;
    }

    .fill-container {
        position: absolute;
        top: 0;
        right: 0;
        width: 100%;
        height: 100%;
    }

    .darken {
        background-color: rgba(119, 136, 153, 0.5);
    }
</style>