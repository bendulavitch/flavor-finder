import { createRouter, createWebHistory } from 'vue-router'
import { useStore } from 'vuex'
import HomeView from '../views/HomeView.vue';
import LoginView from '../views/LoginView.vue';
import LogoutView from '../views/LogoutView.vue';
import RegisterView from '../views/RegisterView.vue';
import AboutView from '../views/AboutView.vue';
import ContactView from '../views/ContactView.vue';
import TermsView from '../views/TermsView.vue';
import FavoritesView from '../views/FavoritesView.vue';
import VoteSessionView from '../views/VoteSessionView.vue'; // New view

const routes = [
  { path: '/', name: 'home', component: HomeView, meta: { requiresAuth: true } },
  { path: "/login", name: "login", component: LoginView, meta: { requiresAuth: false } },
  { path: "/logout", name: "logout", component: LogoutView, meta: { requiresAuth: false } },
  { path: "/register", name: "register", component: RegisterView, meta: { requiresAuth: false } },
  { path: "/about", name: "about", component: AboutView, meta: { requiresAuth: false } },
  { path: "/contact", name: "contact", component: ContactView, meta: { requiresAuth: false } },
  { path: "/terms", name: "terms", component: TermsView, meta: { requiresAuth: false } },
  { path: "/favorites", name: "favorites", component: FavoritesView, meta: { requiresAuth: true } },
  { path: "/vote-session/:roomCode", name: "vote-session", component: VoteSessionView, meta: { requiresAuth: true } }
];

const router = createRouter({
  history: createWebHistory(),
  routes: routes
});

router.beforeEach((to, from, next) => {
  const requiresAuth = to.matched.some((record) => record.meta.requiresAuth);
  const isAuthenticated = !!localStorage.getItem("token");

  if (requiresAuth && !isAuthenticated) {
    next({ name: "login" });
  } else {
    next();
  }
});

export default router;
