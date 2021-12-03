class ProjectView {
	constructor() {
		this.$navProjTitle = $("#projectView #navProjTitle");
		this.$projectTitle = $("#projectView #projectTitle");
		this.$projectTeamList = $("#projectView #projectTeamList");
		this.$projectTaskList = $("#projectView .task-table");
		this.$projectTaskTemplate = $("#projectView #projectTaskTemplate");
		this.fetchProject(window.projectId);
	}
	renderProject(project) {
		this.$navProjTitle.text(project.title);
		this.$projectTitle.text(project.title);
		$('#projectView > .container').addClass('animate__animated animate__fadeIn');

		if (project.tasks) {
			for (const task of project.tasks) this.renderTask(task, null);
		}
		if (project.teammates) {
			for (const teammate of project.teammates) this.renderTeammate(teammate);
		}
	}
	renderTeammate(teammate) {
		$("<li></li>").text(teammate.name).insertBefore($('#projectTeamList .addNewMember'));
	}
	renderTask(task, parent) {
		const $addTo = (parent) ? parent : this.$projectTaskList;
		const $thisTask = this.$projectTaskTemplate.clone();
		$thisTask.removeAttr('id').removeClass('Template');
		if (task.completed) {
			$('.task-completion', $thisTask).html(`<span data-feather="check-square"></span>`);
			$thisTask.addClass('task-done');
		}
		$('.task-number', $thisTask).text(task.taskNumber);
		$('.task-label', $thisTask).text(task.name);
		if (!this.subtasks && task.assigned) {
			for (const assignee of task.assigned) {
				const $thisAssign = $('.task-assign', $thisTask).clone();
				$thisAssign.removeClass('task-assign');
				$thisAssign.text(assignee.name);
				$thisAssign.attr('title', assignee.name);
				$thisAssign.prependTo($('.task-assignment-box', $thisTask));
			}
		}
		if (this.subtasks) {
			$('.task-assignment-box', $thisTask).remove();
			$thisTask.addClass('task-withSubs');
			const $subtaskBox = $('.task-subtasks', $thisTask);
			for (const subtask of this.subtasks) {
				this.renderTask(subtask, $subtaskBox);
			}
		} else $('.task-subtasks', $thisTask).remove();

		$thisTask.insertBefore($('.task-entry-add', $addTo));
	}
	fetchProject() {
		$.get('/projects/'+window.projectId).done( (project) => {
			console.log(project);
			this.renderProject(project);
		}).fail( (err) => {
			console.error("Failed to retrieve project", err);
		});
	}
}

export {
	ProjectView
};
