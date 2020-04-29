import {createMock, jsonResponse} from "./request";

const moment = require('moment');

const buildUri = (path) => {
    return `http://localhost:8080${path}`;
};
const buildDayId = (date = "") => {
    const m = moment(date);
    if (m.isValid()) {
        return m.format("YYYYMMDD")
    }
    throw "Invalid day format " + JSON.stringify(date);
};
const configure = (config = {}) => {
    config.headers = {
        ...(config.headers || {}),
        'Authentication': "<token>"
    };
    return config;
};
/**
 * type Resource
 * {
 *     item: <path in storage>
 * }
 *
 * @param date
 * @param file: Posting
 * @returns {Promise<Resource>}
 */
export const uploadImage = async (date, file) => {
    const day = buildDayId(date);
    const form = new FormData();
    form.append("file", file);
    const resp = await createMock(jsonResponse({
        item: '/2020_01/12/file.jpeg'
    }))(
        buildUri(`/day/${day}/photo`),
        configure({
            headers: {
                'Content-Type': 'multipart/form-data',
            },
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
/**
 * Returns:
 *
 * [ { Posting } ]
 *
 * @returns {Promise<Posting[]>}
 */
export const loadLatestPosts = async () => {
    const resp = await fetch(buildUri(`/postings`), configure());
    return await resp.json();
};
/**
 * Returns: Posting with id
 * {
 *  id
 *  ref_date
 *  body_format
 *  body_content
 *  attachments
 *  access_type
 * }
 * @param date
 * @param posting
 * @returns {Promise<Response>}
 */
export const createPosting = async (date, posting) => {
    const day = buildDayId(date);
    const response = await fetch(buildUri(`/day/${day}/post`), configure({
        headers: {
            'Content-Type': 'applicaton/json',
        },
        method: 'POST',
        body: JSON.stringify(posting),
    }));
    return await response.json();
};
/**
 *
 * @param dateKey
 * @returns {Promise<any>}
 */
export const listImagesOfDate = async (dateKey) => {
    const req = await fetch(buildUri(`/postings/${dateKey}/attachments`), configure({
        method: 'GET',
    }));
    return await req.json();
};