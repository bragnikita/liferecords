<template>
    <div class="wrapper p-2">
        <div class="access py-1 d-flex justify-content-between align-items-start">
            <b-field>
                <b-radio-button v-model="access" native-value="private" type="is-success">
                    <b-icon icon="lock"/>Private
                </b-radio-button>
                <b-radio-button v-model="access" native-value="public" type="is-danger">
                    <b-icon icon="globe"/>Public
                </b-radio-button>
                <b-radio-button v-model="access" native-value="restricted">
                    <b-icon icon="key"/>Restricted
                </b-radio-button>
            </b-field>
            <b-dropdown v-model="bodyFormat">
                <p class="tag is-info" slot="trigger" role="button">{{bodyFormat}}</p>
                <b-dropdown-item value="plain" aria-role="listitem">Plain</b-dropdown-item>
                <b-dropdown-item value="markdown" aria-role="listitem">Markdown</b-dropdown-item>
                <b-dropdown-item value="html" aria-role="listitem">HTML</b-dropdown-item>
            </b-dropdown>
        </div>
        <div class="body mt-3">
            <b-field label="Text">
                <b-input rows="10" type="textarea" v-model="bodyText"></b-input>
            </b-field>
        </div>
        <div class="attachments mt-3">
            <uploader v-model="attachments" />
        </div>
        <div class="options mt-3 d-flex justify-content-start px-1">
            <b-field label="Published date">
                <b-datepicker
                        v-model="publishAt"
                        placeholder="Click to select..."
                        icon="calendar-today"
                        :date-formatter="formatDate"
                        trap-focus>
                </b-datepicker>
            </b-field>
        </div>
    </div>
</template>

<script>

    import moment from 'moment';
    import ImageUploader from "./ImageUploader";

    export default {
        components: {
            uploader: ImageUploader,

        },
        data() {
            return {
                access: "private",
                bodyFormat: "markdown",
                bodyText: "",
                publishAt: new Date(),
                attachments: [],
            }
        },
        methods: {
            formatDate(date) {
                return moment(date).format('YYYY-MM-DD (ddd)');
            }
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
</style>