<template>
    <div class="home pt-3">
        <div v-if="isCreateDialogActive">
            <VkPostEditor :day="selectedDate" @saved="onSaved"/>
        </div>
        <div class="d-flex justify-content-between mb-3" v-else>
            <b-modal :active.sync="isCalendarDialogActive" trap-focus has-modal-card>
                <div class="modal-card" style="width: auto">
                    <header class="modal-card-head">
                        <span>Select date</span>
                    </header>
                    <section class="modal-card-body">
                        <b-datepicker inline v-model="selectedDate"/>
                        <b-button expanded type="is-info" class="mt-2"
                                  @click="openCreateNew()">
                            Create
                        </b-button>
                    </section>
                </div>
            </b-modal>
            <b-button size="is-medium" icon-left="plus" type="is-primary" outlined expanded
                      @click="openCreateNew('today')">
                Add
            </b-button>
            <b-button class="ml-1" size="is-medium" icon-left="calendar-plus" type="is-info" outlined
                      @click="isCalendarDialogActive = true"/>
        </div>
        <div v-if="loading" class="thread-loader position-relative">
            <b-loading active :is-full-page="false"/>
        </div>
        <template v-else>
            <div v-for="day in days" class="day-wrapper" v-bind:key="day.date">
                <div class="day-title">{{day.date}}</div>
                <div class="day-photos">

                </div>
                <div class="day-postings">
                    <div class="posting-wrapper" v-for="posting in day.postings" v-bind:key="posting.id">
                        <Post :post="posting"/>
                        <div>

                        </div>
                    </div>
                </div>
            </div>
        </template>
        <!--        <div class="test-wrapper">-->

        <!--        </div>-->
    </div>
</template>

<script>
    // @ is an alias to /src
    // import Editor from '@/components/VkPostEditor.vue'
    import Post from "../components/Post";
    import _ from 'lodash';
    import VkPostEditor from "../components/VkPostEditor";

    export default {
        name: 'Home',
        components: {
            VkPostEditor,
            Post,
        },
        data: () => ({
            loading: false,
            isCalendarDialogActive: false,
            isCreateDialogActive: false,
            selectedDate: new Date(),
            rawPosts: [],
            days: [],
        }),
        created() {
            this.loading = true;
            this.reload()
                .then((items) => {
                    this.rawPosts = items;
                }).finally(() => this.loading = false);
        },
        methods: {
            async reload() {
                const body = {
                    items: [
                        {
                            "id": "20200112_01",
                            "created_at": 12345,
                            "updated_at": 67842,
                            "ref_date": "20200112",
                            "access_type": "public",
                            "body_format": "plain",
                            "body_content": "There was a good day 1",
                            "attachments": [
                                {
                                    "type": "link",
                                    "title": "Twitter",
                                    "url": "http://twitter.com"
                                },
                                {
                                    "type": "image",
                                    "url": "https://picsum.photos/id/1/1024/680"
                                }
                            ]

                        },
                        {
                            "id": "20200112_02",
                            "created_at": 12345,
                            "updated_at": 67842,
                            "ref_date": "20200112",
                            "access_type": "private",
                            "body_format": "md",
                            "body_content": "# There was a good day 2",
                        },
                        {
                            "id": "20200114_01",
                            "created_at": 12345,
                            "updated_at": 67842,
                            "ref_date": "20200114",
                            "access_type": "private",
                            "body_format": "md",
                            "body_content": "# There was a good day 2",
                        }
                    ]
                };

                return new Promise(resolve => setTimeout(() => {
                    resolve(body.items)
                }, 500));
            },
            openCreateNew(flag) {
                if (flag === 'today') {
                    this.selectedDate = new Date();
                    this.isCreateDialogActive = true;
                } else {
                    this.isCalendarDialogActive = false;
                    this.isCreateDialogActive = true;
                }
            },
            onSaved(post) {

            },
        },

        watch: {
            rawPosts(values) {
                const days = _.groupBy(values, (i) => i.ref_date);
                this.days = Object.keys(days).sort().reverse().map((key) => {
                    return {
                        date: key,
                        postings: _.sortBy(days[key], "id"),
                    }
                });
            }
        }

    }
</script>
<style lang="scss">
    .home {
        max-width: 600px;
        min-width: 400px;
        margin: auto;
    }

    .test-wrapper {
        border: 1px solid gray;
        display: block;
        max-width: 500px;
        margin: auto;
    }

    .day-title {
        font-size: large;
        text-align: left;
        padding-left: 10px;
        color: #7d5900;
        box-shadow: 1px 3px 1px -1px rgba(0, 0, 0, 0.58);
        font-weight: bold;
        font-style: italic;
        border-bottom: 1px solid #7d5900;
    }

    .day-postings {
        padding: 5px 10px;
    }

    .posting-wrapper {
        padding: 5px;
        border: 1px gray solid;
        box-shadow: 6px 7px 5px -2px rgba(0, 0, 0, 0.58);
        margin-bottom: 5px;
    }

    .day-photos {
        padding: 5px 10px;
    }

    .thread-loader {
        width: 100%;
        min-height: 100px;
    }

</style>