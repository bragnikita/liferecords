const buildUri = (path) => {
    return `http://localhost:8080${path}`;
};
const configure = (config = {}) => {
    config.headers = {
        ...(config.headers || {}),
        'Authentication': "<token>"
    };
    return config;
};

export const uploadImageForPost = async (file, postId) => {
    const form = new FormData();
    form.append("file", file);
    const resp = await fetch(
        buildUri(`/postings/${postId}/attachments`),
        configure({
            method: 'POST',
            body: form,
        })
    );
    return await resp.json();
};

export const deleteImageForPost = async (objectId) => {
    await fetch(buildUri(`/attachments/${objectId}`), configure({
        method: 'DELETE'
    }))
};

export const listImagesOfDate = async (dateKey) => {
    const req = await fetch(buildUri(`/postings/${dateKey}/attachments`), configure({
        method: 'GET',
    }));
    return await req.json();
};