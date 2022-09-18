import React, {useEffect, useState} from "react";
import {useNavigate, Link} from "react-router-dom";
import TasksService from "../../../services/TasksService";

const CreateTaskForm = () => {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [importance, setImportance] = useState('');
  const [urgency, setUrgency] = useState('');
  const [priority, setPriority] = useState('NORMAL');
  const [dueDate, setDueDate] = useState('');
  const [project, setProject] = useState('');

  const [ListProjects, setListProjects] = useState([]);
  const navigate = useNavigate();

  const saveTask = (e) => {
    e.preventDefault();
    if (project === '') {
      const task = {name, description, priority, dueDate}
      console.log(task);
      TasksService.createTask(task).then((response) => {
        console.log(response.data);
        navigate("/show-tasks");
      })
    } else {
      let projects = JSON.parse(project);
      const task = {name, description, priority, dueDate, projects}
      TasksService.createTask(task).then((response) => {
        navigate("/show-tasks");
      })
    }
  };

  useEffect(() => {
    TasksService.getAllProjects().then((res) => {
      setListProjects(res.data);
    });
  }, [])

  return (
    <div className="container">
      <div className="col-md-8 order-md-1">
        <nav aria-label="breadcrumb">
          <ol className="breadcrumb">
            <li className="breadcrumb-item"><Link to="/show-tasks">Back to task list</Link></li>
            <li className="breadcrumb-item active" aria-current="page">create task</li>
          </ol>
        </nav>
        <form>
          <div className="col-md-5 mb-3">
            <label className="form-label">Name</label>
            <input
              type="text"
              name="name"
              className="form-control"
              placeholder="Name..."
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
            {/*<div className="invalid-feedback">Please enter task name.</div>*/}
          </div>
          <div className="col-md-5 mb-3">
            <label>
              Description<span className="text-muted">(Optional)</span>
            </label>
            <textarea
              name="description"
              className="form-control"
              placeholder="Description.."
              style={{height: "150px"}}
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            >Description</textarea>
          </div>
          <div className="col-md-5 mb-3">
            <label>Importence</label>
            <select
              className="form-select"
              name="importance"
              value={importance}
              onChange={(e) => setImportance(e.target.value)}
            >
              <option value="IMPORTANT">Important</option>
              <option value="NOT_IMPORTANT">Not important</option>
            </select>
          </div>
          <div className="col-md-5 mb-3">
            <label>Urgency</label>
            <select
              className="form-select"
              name="urgency"
              value={urgency}
              onChange={(e) => setUrgency(e.target.value)}
            >
              <option value="URGENT">Urgent</option>
              <option value="NOT_URGENT">Not urgent</option>
            </select>
          </div>
          <div className="col-md-5 mb-3">
            <label>Priority</label>
            <select
              className="form-select"
              name="priority"
              value={priority}
              onChange={(e) => setPriority(e.target.value)}
            >
              <option value="NORMAL">Normal</option>
              <option value="HIGH">High</option>
              <option value="LOW">Low</option>
              <option value="PAUSE">Pause</option>
            </select>
          </div>
          <div className="col-md-5 mb-3">
            <label>Due date</label>
            <input
              type="datetime-local"
              className="form-control"
              name="dueDate"
              value={dueDate}
              onChange={(e) => setDueDate(e.target.value)}
            />
          </div>
          <div className="col-md-5 mb-3">
            <label>Project</label>
            <select className="form-select" name="taskProject"
                    onChange={(e) => setProject(e.target.value)}>
              <option value="">Choose project</option>
              {ListProjects.map((project) =>
                <option key={project.id} value={JSON.stringify(project)}>
                  {project.name}
                </option>
                //TODO fix me use id instead of object
              )}
            </select>
          </div>
          <button
            className="btn btn-primary"
            onClick={(e) => saveTask(e)}>Save
          </button>
        </form>
      </div>
    </div>
  );
};

export default CreateTaskForm;