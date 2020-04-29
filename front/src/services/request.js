import _ from 'lodash';

const delay = async (callback) => {
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            resolve(callback())
        }, 1000)
    });
};

export const createMock = (responseObject) => {

    const mockRequest = async (urlString, params) => {
        console.log(
            `Request to [${urlString}] \n` +
            JSON.stringify(params)
        );
        return delay(() => responseObject);
    };

    return mockRequest;
};

export const jsonResponse = (json) => {
    return {
        json: async () => json
    }
};