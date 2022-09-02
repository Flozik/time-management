import axios from "axios";

const INDEX_URL = "http://localhost:8080/";
const CREATETASK_URL = "http://localhost:8080/create-task";
const SHOWTASKS_URL = "http://localhost:8080/show-tasks/page/1";
const GET_ALL_PROJECTS_URL = "http://localhost:8080/get-all-projects";
const CREATEPROJECT_URL = "http://localhost:8080/add-project";
const DELETETASK_URL = "http://localhost:8080/delete-task";
const SHOWTASKBYID_URL = "http://localhost:8080/task";
const CHANGESTATUS_URL = "http://localhost:8080/show-tasks/task/update";
const UPDATETASK_URL = "http://localhost:8080/task/update";
const SEARCHTASKS_URL = "http://localhost:8080/show-tasks/searchBy="; //TODO


class TasksService {
  getMainPage() {
    return axios.get(INDEX_URL);
  }

  createTask(task) {
    return axios.post(CREATETASK_URL, task);
  }

  getShowTasksPage() {
    return axios.get(SHOWTASKS_URL);
  }

  getAllProjects() {
    return axios.get(GET_ALL_PROJECTS_URL);
  }

  createProject(project) {
    return axios.post(CREATEPROJECT_URL, project);
  }

  deleteTask(taskId) {
    return axios.post(DELETETASK_URL + "/" + taskId);
  }

  getTask(taskId) {
    return axios.get(SHOWTASKBYID_URL + "/" + taskId);
  }

  changeStatus(taskId, status) {
    return axios.post(CHANGESTATUS_URL + "/" + taskId, status);
  }

  updateTask(taskId, task) {
    return axios.post(UPDATETASK_URL + "/" + taskId, task);
  }

  findTasks(keyWord) {
    return axios.get(SEARCHTASKS_URL + keyWord);
  }
}

export default new TasksService();