<template>
  <div>
    <a-tag color="blue">Cantidad de Roles registrados</a-tag>
    <a-badge :count="countRole" />
    <br />
    <br />
    <RoleFormVue v-if="isAdmin" @update-list="fetchRoles" />
    <hr />

    <a-table bordered :data-source="listRole" :columns="columns">
      <template #bodyCell="{ column, text, record }">
        <template v-if="column.dataIndex === 'name'">
          <div class="editable-cell">
            <div class="editable-cell-text-wrapper">
              {{ text || " " }}
            </div>
          </div>
        </template>
        <template v-else-if="column.dataIndex === 'actions' && isAdmin">
          <a-popconfirm
            v-if="listRole.length"
            :title="'¿Está seguro que quiere eliminar el rol ' + record.name"
            @confirm="onDelete(record.id, record.name)"
          >
            <a-radio-button value="large">
              <delete-outlined />
            </a-radio-button>
          </a-popconfirm>
        </template>
      </template>
    </a-table>
  </div>
</template>
<script setup>
import { useUserStore } from "../stores/Users";
import RoleFormVue from "../components/RoleForm.vue";
import { ref, onMounted, computed } from "vue";
import { DeleteOutlined } from "@ant-design/icons-vue";
import { getAllRoles, removeRole } from "../composables/Role";
import { successNotification, errorNotification } from "../composables/Notification";

const columns = [
  {
    title: "ID",
    dataIndex: "id",
  },
  {
    title: "Nombre",
    dataIndex: "name",
  },
  {
    title: "Descripción",
    dataIndex: "description",
  },
  {
    title: "Acciones",
    dataIndex: "actions",
  },
];
const userStore = useUserStore();

const listRole = ref([]);
const countRole = computed(() => listRole.value.length);

const isAdmin = computed(() => {
  const roleList = userStore.getRolePermissions;
  const role = roleList.find((ele) => ele === "ADMIN");
  return role;
});

const onDelete = async (roleId, name) => {
  try {
    await removeRole(roleId);
    successNotification("Eliminación exitosa!", `El rol ${name} fue eliminado.`);
    await fetchRoles();
  } catch (e) {
    if (e.response) {
      const data = await e.response.json();
      if (data.errors) {
        errorNotification("Ocurrió un error :(", data.errors[0].defaultMessage);
        return;
      }
      errorNotification("Ocurrió un error al eliminar el rol :(", data.message);
    }
  }
};

const fetchRoles = async () => {
  const result = await getAllRoles();
  const data = await result.json();
  listRole.value = data;
};

onMounted(async () => {
  await fetchRoles();
});
</script>
