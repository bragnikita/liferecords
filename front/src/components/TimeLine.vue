<template>
    <div v-for="day in days" :key="day.date">
        <div class="dayline">{{ formatDay(day)}}</div>
        <div class="posts-container">
            <div class="post-container" v-for="post in day.posts" :key="post.id">
                <post :post="post"/>
            </div>
        </div>
    </div>
</template>

<script>
    import moment from "moment";
    import Post from "./Post";

    /*
    * type Day {
    * posts: Post[],
    * date: iso date,
    *
    * }
    */
    export default {
        name: "TimeLine",
        components: {Post},
        props: {
            fetchFromDay: {
                // async (date: Iso day | undefined) => Day[]
                type: Function,
                required: true,
            }
        },
        data: function () {
            return {
                loading: false,
                days: [],
            }
        },
        created() {
            this.loading = true;
            this.fetchFromDay().then((days) => {
                this.days = days;
            }).finally(() => {
                this.loading = false;
            });
        },
        methods: {
            formatDay(day) {
                return moment(day).format('L');
            }
        },
    }
</script>

<style scoped>

</style>