<template>

    <div class="d-flex flex-wrap py-1">
        <div v-for="image in files" :key="image.id" class="app-file is-relative">
            <b-button size="is-small" icon-right="times" class="app-delete" v-if="image.status === 'done'" @click="image.remove()"/>
            <preview :preview-src="image.preview" :loading="false" class="fullsize" v-if="image.status === 'done'"/>
            <preview :preview-src="image.preview" :loading="true" class="fullsize" v-if="image.status !== 'done'"/>
        </div>
        <b-field class="file ">
            <b-upload multiple v-model="attached">
                <a class="button app-btn">
                    <b-icon icon="plus"/>
                </a>
            </b-upload>
        </b-field>
    </div>

</template>


<script>

    /*
    * file: {
    * uri - string
    * file - File
    * id - string
    * filename - string
    * status - added, uploading, done
    * preview - string
    * uploadIt - func
    * deleteIt - func
    * }
    *
    *
    * */
    import FilePreview from "./FilePreview";
    import {uploadImageForPost} from "../services/apiClient";

    export default {
        components: {
            preview: FilePreview,
        },
        data: function () {
            return {
                files: this.makeModel(this.value),
                attached: [],
            }
        },
        props: {
            value: {
                type: Array,
                default: () => []
            },
        },
        name: "ImageUploader",
        computed: {
            filesDone() {
                return this.files.filter((f) => f.status === 'done')
            },
            filesProcess() {
                return this.files.filter((f) => f.status !== 'done')
            }
        },
        methods: {
            addImage() {

            },
            makeModel(urls) {
                const self = this;
                return urls.map((url) => {
                    const existed = self.files.find((f) => f.uri === url);
                    if (existed) {
                        return existed;
                    }
                    return {
                        uri: url,
                        id: generateKey(),
                        status: 'done',
                        remove: function () {
                            self.remove(this)
                        }
                    }
                })
            },
            makeReturnArg(files) {
              return files.filter((f) => f.status === 'done').map((f) => {
                  //todo
                  return f.uri
              })
            },
            remove(file) {
                //TODO
                this.files.splice(this.files.indexOf(file), 1);
                this.$emit('input', this.makeReturnArg(this.files));
            }
        },
        watch: {
            value(value) {
                const inProgress = this.files.filter((f) => f.status !== "done");
                this.files = this.makeModel(value).concat(inProgress);
            },
            files(files) {
                const self = this;
                files.filter((f) => f.status === 'added').forEach((f) => {
                    f.status = 'uploading';
                    function cb(uri) {
                        f.status = 'done';
                        f.uri = uri;
                        self.$emit('input', self.makeReturnArg(self.files));
                    }
                    uploadImageForPost(f.file, "2020_03/16").then((descr) => {
                        cb(descr.url)
                    })
                })
            },
            attached(files) {
                console.log(files);
                if (files.length === 0) return;
                const self = this;
                const mapped = files.map((f) => {
                    return {
                        file: f,
                        filename: f.name,
                        id: generateKey(),
                        status: 'added',
                        remove() {
                            self.remove(this)
                        }
                    }
                });
                mapped.forEach((f) => {
                    const reader = new FileReader();
                    reader.addEventListener("load", function () {
                        f.preview = reader.result;
                        self.$forceUpdate();
                    }, false);
                    if (f.file) {
                        reader.readAsDataURL(f.file)
                    }
                });
                this.files.push(...mapped);
                this.attached.splice(0, this.attached.length);
            },
        }
    }

    function generateKey() {
        return `${Date.now()}${Math.random().toString(16)}`
    }
</script>

<style scoped lang="scss">
    .app-file, .app-attached, .app-btn {
        border: 1px solid gray;
        width: 100px;
        height: 100px;
        overflow: hidden;
        margin-right: 5px;
        margin-bottom: 5px;
        word-break: break-word;
        border-radius: 5px;
    }

    .app-delete {
        position: absolute;
        top: 3px;
        right: 3px;
        z-index: 1;
    }

    .fullsize {
        position: absolute;
        top: 0;
        left: 0;
        height: 100%;
        width: 100%;
    }
</style>