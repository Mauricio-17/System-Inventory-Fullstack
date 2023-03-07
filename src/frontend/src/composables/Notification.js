import { notification } from 'ant-design-vue';

export const successNotification = (title, message) => {
  notification.success({
    message: title,
    description: message
  });
};

export const errorNotification = (title, message) => {
    notification.error({
        message: title,
        description: message
      });
};
