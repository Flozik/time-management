import React from "react";
import {Badge} from "react-bootstrap";
import SetBadge from "../../../scripts/SetBadge.js";
import {Link} from "react-router-dom";
import ChangeStatus from "../../../scripts/ChangeStatus.js";

const TasksTableRows = ({
                          task: {id, name, description, created, status, progress, dueDate, priority, projectName, importance, urgency},
                          deleteTask, setStatus
                        }) => {
  return (
    <tr>
      <td><Link to={"/task/" + id}>{name}</Link></td>
      <td onClick={() => {
        ChangeStatus(id, status, setStatus)
      }}>{status}</td>
      <td>{importance}</td>
      <td>{urgency}</td>
      {/*<td>
        <div className="progress">
          <div
            className="progress-bar"
            role="progressbar"
            style={{width: progress + "%"}}
          >
            {progress}
          </div>
        </div>
      </td>*/}
      {/*{
        dueDate !== null
          ? <td>{dueDate}</td>
          : <td>Not assigned</td>
      }*/}
      <td><Badge bg={SetBadge(priority)}>{priority}</Badge></td>
      {projectName !== null
        ? <td>{projectName["name"]}</td>
        : <td></td>
      }
      <td>
        <button type="button" className="btn btn-danger" onClick={() => {
          if (window.confirm("Are you really want delete this task?")) deleteTask(id)
        }}>Delete
        </button>
      </td>
    </tr>
  )
}

export default TasksTableRows;