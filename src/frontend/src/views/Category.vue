<template>
    <div>
        <a-tag color="blue">Cantidad de Categorias</a-tag>
        <a-badge :count="countCategory" />
        <br />
        <br />
        <CategoryFormVue @update-list="fetchCategories" />
        <hr>

        <a-table bordered :data-source="listCategories" :columns="columns">
            <template #bodyCell="{ column, text, record }">
                <template v-if="column.dataIndex === 'name'">
                    <div class="editable-cell">
                        <div class="editable-cell-text-wrapper">
                            {{ text || ' ' }}
                        </div>
                    </div>
                </template>
                <template v-else-if="column.dataIndex === 'actions'">
                    <a-popconfirm v-if="listCategories.length"
                        :title="'¿Está seguro que quiere eliminar a ' + record.name" @confirm="onDelete(record.id, record.name)">
                        <a-radio-button value="large">
                            <delete-outlined />
                        </a-radio-button>
                    </a-popconfirm>
                    <CategoryFormVue @update-list="fetchCategories" :record="record" />
                </template>
            </template>
        </a-table>

    </div>
</template>
<script setup>
import CategoryFormVue from '../components/CategoryForm.vue';
import { ref, onMounted, computed } from 'vue';
import { DeleteOutlined } from '@ant-design/icons-vue';
import { getAllCategories, removeCategory } from '../composables/Category';
import { successNotification, errorNotification } from '../composables/Notification';

const columns = [
    {
        title: 'ID',
        dataIndex: 'id',
    },
    {
        title: 'Nombre',
        dataIndex: 'name',
    },
    {
        title: 'Descripción',
        dataIndex: 'description',
    },
    {
        title: 'Acciones',
        dataIndex: 'actions'
    }
];

const listCategories = ref([]);
const countCategory = computed(() => listCategories.value.length);

const onDelete = async (categoryId, name) => {
    try {
        const result = await removeCategory(categoryId);
        successNotification("Eliminación exitosa!", `La categoría ${name} fue eliminada.`);
        await fetchCategories();
    }
    catch (e) {
        if (e.response) {
            const data = await e.response.json();
            if (data.errors){
                errorNotification("Ocurrió un error :(", data.errors[0].defaultMessage);
                return;
            }
            errorNotification("Ocurrió un error :(", data.message);
        }

    }
};

const fetchCategories = async () => {
    const result = await getAllCategories();
    const data = await result.json();
    listCategories.value = data;
};

onMounted(async () => {
    await fetchCategories();
});

</script>

