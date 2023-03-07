<template>
  <a-layout>
    <a-layout-header class="header">
      <div class="logo" />
      <a-menu
        class="d-flex flex-row-reverse"
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
      >
        <!-- <div class="d-flex flex-row mb-3">
            <a-menu-item key="1">nav 1</a-menu-item>
            <a-menu-item key="2">nav 2</a-menu-item>
            <a-menu-item key="3">nav 3</a-menu-item>
          </div>
          <div> -->
        <a-menu-item key="4">
          <!-- <a-popconfirm title="¿Está seguro que desea cerrar sesión ?"
              @confirm="logOut">
              <a-button ghost>
                LOG OUT
              </a-button>
            </a-popconfirm> -->
          <a-space>
            <a-button v-if="token" @click="showConfirm"> Cerrar Sesión </a-button>
            <a-button v-if="!token">
              <RouterLink active-class="active" to="/login"> LOGIN </RouterLink>
            </a-button>
            <a-button v-if="!token">
              <RouterLink active-class="active" to="/register"> REGISTRO </RouterLink>
            </a-button>
          </a-space>
        </a-menu-item>
        <!-- </div> -->
      </a-menu>
    </a-layout-header>
    <a-layout style="min-height: 100vh" v-if="token">
      <a-layout-sider v-model:collapsed="collapsed" collapsible>
        <div class="logo" />
        <a-menu v-model:selectedKeys="selectedKeys" theme="dark" mode="inline">
          <a-sub-menu key="sub1">
            <template #title>
              <span>
                <bulb-outlined />
                <span>Equipamento</span>
              </span>
            </template>
            <a-menu-item key="3">
              <RouterLink active-class="active" to="/category"> Categoria </RouterLink>
            </a-menu-item>
            <a-menu-item key="4">
              <RouterLink active-class="active" to="/equipment"> Equipamento </RouterLink>
            </a-menu-item>
            <a-menu-item key="13">
              <RouterLink active-class="active" to="/brand"> Marca </RouterLink>
            </a-menu-item>
            <a-menu-item key="5">
              <RouterLink active-class="active" to="/shelf"> Estante </RouterLink>
            </a-menu-item>
            <a-menu-item key="14">
              <RouterLink active-class="active" to="/location"> Ubicación </RouterLink>
            </a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="sub2">
            <template #title>
              <span>
                <team-outlined />
                <span>Administración</span>
              </span>
            </template>
            <a-menu-item key="8">
              <RouterLink active-class="active" to="/role">Rol</RouterLink>
            </a-menu-item>
            <a-menu-item key="9">
              <RouterLink active-class="active" to="/employee">Empleado</RouterLink>
            </a-menu-item>
            <a-menu-item key="6">
              <RouterLink active-class="active" to="/transaction">
                Transacciones
              </RouterLink>
            </a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="sub3" v-if="isAdmin">
            <template #title>
              <span>
                <user-outlined />
                <span>Propietario</span>
              </span>
            </template>
            <a-menu-item key="10">
              <RouterLink active-class="active" to="/type_owner"
                >Tipo propietario</RouterLink
              >
            </a-menu-item>
            <a-menu-item key="11">
              <RouterLink active-class="active" to="/owner">Propietario</RouterLink>
            </a-menu-item>
          </a-sub-menu>
        </a-menu>
      </a-layout-sider>
      <a-layout-content style="margin: 0 16px">
        <a-breadcrumb style="margin: 16px 0">
          <a-breadcrumb-item>User</a-breadcrumb-item>
          <a-breadcrumb-item>Bill</a-breadcrumb-item>
        </a-breadcrumb>
        <div :style="{ padding: '24px', background: '#fff', minHeight: '360px' }">
          <RouterView />
        </div>
      </a-layout-content>
      <!-- <a-layout-content v-else style="margin: 0 16px">
        <div :style="{ padding: '24px', background: '#fff', minHeight: '360px' }">
          <RouterView />
        </div>
      </a-layout-content> -->
    </a-layout>
    <a-layout v-else>
      <RouterView />
    </a-layout>
  </a-layout>
</template>

<script setup>
import { useUserStore } from "./stores/Users";
import { useRouter } from "vue-router";
import { ref, onMounted, computed, onBeforeUnmount } from "vue";
import { ExclamationCircleOutlined } from "@ant-design/icons-vue";
import { Modal } from "ant-design-vue";
import { BulbOutlined, TeamOutlined, UserOutlined } from "@ant-design/icons-vue";

const userStore = useUserStore();

const showConfirm = () => {
  Modal.confirm({
    title: "¿Desea cerrar sesión?",

    onOk() {
      logOut();
    },
    onCancel() {
      console.log("Cancel");
    },
    class: "test",
  });
};

const router = useRouter();

const collapsed = ref(false);
const selectedKeys = ref(["1"]); // for sidebar
const token = computed(() => userStore.JWT); //to check if there's a token or not

const isAdmin = computed(() => {
  const roleList = userStore.getRolePermissions;
  const role = roleList.find((ele) => ele === "ADMIN");
  return role;
});

const logOut = () => {
  localStorage.removeItem("token");
  localStorage.removeItem("email");

  userStore.setJWT(null);
  userStore.setEmail(null);

  window.location.reload();
};

onMounted(() => {
  userStore.setJWT(localStorage.getItem("token"));
});
</script>

<style scoped>
#components-layout-demo-side .logo {
  height: 32px;
  margin: 16px;
  background: rgba(255, 255, 255, 0.3);
}

.site-layout .site-layout-background {
  background: #fff;
}

[data-theme="dark"] .site-layout .site-layout-background {
  background: #141414;
}
</style>

<style scoped></style>
