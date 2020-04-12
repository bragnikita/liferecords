import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import {PostPreview} from "../views/ComponentTest";

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/test/post',
    name: 'test post display',

    component: PostPreview,
  },
  {
    path: '/about',
    name: 'About',
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

export default router
