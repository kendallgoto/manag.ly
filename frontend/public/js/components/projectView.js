class ProjectView {
	constructor() {
		this.$navProjTitle = $("#projectView #navProjTitle");
		this.$projectTitle = $("#projectView #projectTitle");
		this.$projectTeamList = $("#projectView #projectTeamList");
		this.$projectTaskList = $("#projectView .task-table");
		this.$projectTaskTemplate = $("#projectView #projectTaskTemplate");
		this.$addTaskTemplate = $("#projectView #addTaskTemplate");
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
		const $thisTeammate = $(document.createElement("li")).text(teammate.name);
		$thisTeammate.insertBefore($('#projectTeamList .addNewMember'));
	}
	renderTask(task, parent) {
		const $addTo = (parent) ? parent : this.$projectTaskList;
		const $thisTask = this.$projectTaskTemplate.clone();
		$thisTask.insertBefore($('> .task-entry-add', $addTo));
		$thisTask.removeAttr('id').removeClass('Template').data('taskId', task.id);
		if (task.completed) {
			$('> .task-completion', $thisTask).html(`<span data-feather="check-square"></span>`);
			$thisTask.addClass('task-done');
		} else {
			$('> .task-completion', $thisTask).html(`<span data-feather="square"></span>`);
			$thisTask.removeClass('task-done');
		}
		$('> .task-number', $thisTask).text(task.taskNumber);
		$('> .task-label', $thisTask).text(task.name);
		if (!task.subtasks?.length && task.assigned) {
			for (const assignee of task.assigned) {
				const $thisAssign = $('> .task-assign', $thisTask).clone();
				$thisAssign.removeClass('task-assign');
				$thisAssign.text(assignee.name);
				$thisAssign.attr('title', assignee.name);
				$thisAssign.prependTo($('> .task-assignment-box', $thisTask));
			}
		}
		if (task.subtasks?.length) {
			$('> .task-completion', $thisTask).remove();
			$('> .task-assignments .task-assignment-box', $thisTask).remove();
			$thisTask.addClass('task-withSubs');
			const $subtaskBox = $('> .task-subtasks', $thisTask);
			for (const subtask of task.subtasks) {
				this.renderTask(subtask, $subtaskBox);
			}
		} else $('> .task-subtasks', $thisTask).remove();

	}
	fetchProject() {
		$.get('/projects/' + window.projectId).done((project) => {
			console.log(project);
			this.renderProject(project);
			window.feather.replace();
			this.registerHandlers();
		}).fail((err) => {
			this.$navProjTitle.text("404: Project not found!");
			console.error("Failed to retrieve project", err);
		});
	}
	addNewTask($taskAdder) {
		const $taskField = $('.task-label input', $taskAdder);
		const $taskButton = $('.task-edit-btn', $taskAdder);
		const $parent = $taskAdder.parent();
		if ($taskField[0].reportValidity && !$taskField[0].reportValidity()) return;
		$taskButton.prop('disabled', true);

		const newTask = $taskField.val().trim();
		const parentId = $parent.data('taskId');
		const projectId = window.projectId;

		$.post('/tasks', JSON.stringify({
			name: newTask,
			projectId: projectId,
			parentId: parentId
		})).done((resp) => {
			console.log("Created new task", resp);
			$taskAdder.remove();
			this.renderTask(resp, $parent);
			this.registerHandlers();
		}).fail((err) => {
			console.log("Failed to create new task", err);
			$taskField
				.addClass('is-invalid')
				.val("")
				.attr('placeholder', err.responseJSON?.message ?? "An unknown error occurred. Please try again.");
			$taskButton.prop('disabled', false);
		});
	}
	setupTaskAdder($addButton) {
		const $addList = $addButton.parent();
		const $alreadyAdding = $('> .task-entry-adding:not(.Template)', $addList);
		if ($alreadyAdding.length) {
			console.log("already adding in progress!");
			$alreadyAdding.removeClass('animate__animated animate__flash');
			setTimeout(function () { // allow for page refresh
				$alreadyAdding.addClass('animate__animated animate__flash');
			}, 5);
			return;
		}
		const $thisToAdd = this.$addTaskTemplate.clone();
		$thisToAdd.removeAttr('id').removeClass('Template');
		$thisToAdd.addClass('animate__animated animate__fadeInRight');
		// figure oiut how many already exist ...
		const $knownTasks = $('> .task-entry:not(.Template):not(.task-entry-add)', $addList);
		const firstPath = $('> .task-number', $knownTasks).first().text();
		console.log(firstPath);
		let pathArray = firstPath.split('.');
		pathArray = pathArray.slice(0, -2); // remove last two elements
		pathArray.push($knownTasks.length + 1);
		pathArray.push("");
		const finalPath = pathArray.join('.');
		$('.task-number', $thisToAdd).text(finalPath);

		$('.task-edit-btn', $thisToAdd).click(() => {
			this.addNewTask($thisToAdd);
		});
		$thisToAdd.insertBefore($addButton);
	}
	markTask($task) {
		const $completion = $('> .task-completion', $task);
		if ($completion.hasClass('disabled')) {
			return;
		}
		$completion.addClass('disabled', true);
		const isComplete = (!($('> .feather-check-square', $completion).length)).toString;
		const taskId = $task.data('taskId');

		$.post('/tasks/' + taskId + '/completed/' + isComplete).done((resp) => {
			console.log("Update marking", resp);
		}).fail((err) => {
			console.log("Failed to mark", err);
		});
	}
	registerHandlers() {
		$('.task-entry-add').off('click').on('click', (e) => {
			const $addTo = $(e.currentTarget);
			this.setupTaskAdder($addTo);
		});
		$('.task-completion').off('click').on('click', (e) => {
			const $taskToMark = $(e.currentTarget).closest('.task-entry');
			this.markTask($taskToMark)
		});
	}
}

export {
	ProjectView
};
