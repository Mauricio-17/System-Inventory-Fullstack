<template>
  <div>
    <a-tag color="blue">Cantidad de Transacciones</a-tag>
    <a-badge :count="transactionOwner" />
    <br />
    <br />
    <TransactionFormVue 
    :employees="employeeList"
    :equipments="equipmentList"
    @update-list="fetchTransactions" />
    <hr>

    <a-table bordered :data-source="transactionList" :columns="columns" :scroll="{ x: 1500, y: 400 }">
      <template #bodyCell="{ column, text, record }">
        <template v-if="column.dataIndex === 'name'">
          <div class="editable-cell">
            <div class="editable-cell-text-wrapper">
              {{ text || ' ' }}
            </div>
          </div>
        </template>
        <template v-else-if="column.dataIndex === 'operation'">
          <a-popconfirm v-if="transactionList.length" :title="'¿Está seguro que quiere eliminar la transacción de ' + record.equipmentName"
            @confirm="onDelete(record.id, record.equipmentName)">
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
import TransactionFormVue from '../components/TransactionForm.vue';
import { ref, computed, onMounted } from 'vue';
import { DeleteOutlined } from '@ant-design/icons-vue';
import { getAllTransactions, removeTransaction } from '../composables/Transaction';
import { getAllEmployees } from '../composables/Employee';
import { getAllEquipments } from '../composables/Equipment';
import { successNotification, errorNotification } from '../composables/Notification';

const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    fixed: 'left',
    width: 50,
    key: 'id'
  },
  {
    title: 'Descripción',
    dataIndex: 'description',
    fixed: 'left',
    width: 90,
    key: 'description',
  },
  {
    title: 'Equipo',
    dataIndex: 'equipmentName',
    width: 100,
    key: '1'
  },
  {
    title: 'Empleado',
    dataIndex: 'employeeName',
    width: 100,
    key: '2'
  },
  {
    title: 'Procedencia',
    dataIndex: 'sourceRow',
    width: 50,
    key: '3'
  },
  {
    title: 'Destino',
    dataIndex: 'destinationRow',
    width: 50,
    key: '4'
  },
  {
    title: 'Creado en',
    dataIndex: 'createdAt',
    width: 100,
    key: '5'
  },
  {
    title: 'Operaciones',
    dataIndex: 'operation',
    fixed: 'right',
    width: 80,
    key: 'operation'
  },
];



const employeeList = ref([]);
const equipmentList = ref([]);

const transactionList = ref([]);

const transactionOwner = computed(() => transactionList.value.length);

const onDelete = async (transactionId, equipmentName) => {
  try {
    await removeTransaction(transactionId);
    successNotification("Eliminación exitosa!", `La transacción de ${equipmentName} fue eliminada`);
    await fetchTransactions();
  }
  catch (e) {
    if (e.response) {
      const data = await e.response.json();
      if (data.errors) {
        errorNotification("Ocurrió un error :(", data.errors[0].defaultMessage);
        return;
      }
      errorNotification("Ocurrió un error al eliminar :(", data.message);
    }
  }
};

const fetchTransactions = async () => {
  const result = await getAllTransactions();
  const data = await result.json();
  transactionList.value = data.map(item => {
    item["sourceRow"] = item.source.row;
    item["destinationRow"] = item.destination.row;
    return item;
  });
}

onMounted(async () => {
  await fetchTransactions();
  //recibiendo tipos de propietario
  let result = await getAllEquipments();
  let data = await result.json();
  equipmentList.value = data.content;

  result = await getAllEmployees();
  data = await result.json();
  employeeList.value = data;

});

</script>


