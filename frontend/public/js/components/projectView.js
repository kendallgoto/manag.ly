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
		$('.task-edit-btn', $thisTask).click(() => {
			this.startEditingTask($thisTask);
		});
	}
	startEditingTask($task) {
		console.log($task);
		const taskValue = $('> .task-label', $task).text();
		const taskNumber = $('> .task-number', $task).text();

		const $thisToAdd = this.$addTaskTemplate.clone();
		$thisToAdd.removeAttr('id').removeClass('Template').data('taskId', $task.data('taskId'));
		$('> .task-number', $thisToAdd).text(taskNumber);
		$('> .task-label input', $thisToAdd).val(taskValue);
		if ($('> .task-subtasks', $task).children().length) {
			//has subclasses
			$('.task-divide-num, .task-divide-btn', $thisToAdd).remove();
		}
		$('.task-edit-btn', $thisToAdd).click(() => {
			this.saveEditedTask($thisToAdd);
		});
		$('.task-divide-btn', $thisToAdd).click(() => {
			this.divideTask($thisToAdd);
		});
		$thisToAdd.data('originalElement', $task);
		$task.replaceWith($thisToAdd);
	}
	divideTask($task) {
		if ($('.task-divide-btn', $task).prop('disabled')) return;
		$('.task-divide-btn', $task).prop('disabled', true);
		const $subtasks = $(document.createElement("div")).addClass("task-subtasks");
		const divisions = $('.task-divide-num', $task).val();
		const baseNumber = $('.task-number', $task).text();
		$('.task-divide-num, .task-divide-btn, .task-edit-btn', $task).remove();
		$('.task-label input', $task).prop('disabled', true);
		for (let i = 0; i < divisions; i++) {
			const $newSub = this.$addTaskTemplate.clone();
			$newSub.removeAttr('id').removeClass('Template');
			$('> .task-number', $newSub).text(baseNumber + (i + 1) + ".");
			$('> .task-label input', $newSub).val("");
			$('.task-divide-num, .task-divide-btn', $newSub).remove();
			$newSub.appendTo($subtasks);
			if (i + 1 != divisions) {
				$('.task-edit-btn', $newSub).remove();
			} else {
				$('.task-edit-btn', $newSub).click(() => {
					console.log("Save divided tasks");
					this.saveDividedTask($task);
				});
			}
		}
		$subtasks.appendTo($task);
	}
	saveDividedTask($task) {
		const subtasks = [];
		for (const $subtask of $('.task-subtasks .task-label input', $task).toArray()) {
			if ($subtask.reportValidity && !$subtask.reportValidity()) return;
			subtasks.push({
				"name": $subtask.value
			});
		}
		if (subtasks.length) {
			const baseTask = $task.data('taskId');
			if ($('.task-subtasks .task-edit-btn', $task).prop('disabled')) return;
			$('.task-subtasks .task-edit-btn', $task).prop('disabled', true);
			console.log("posting", subtasks);
			$.post({
				url: '/tasks/' + baseTask + '/decompose',
				data: JSON.stringify({ subtasks }),
				contentType: 'application/json'
			}).done((e) => {
				this.fetchProject(window.projectId);
			}).fail((e) => {
				console.error(e);
			})
		}
	}
	saveEditedTask($task) {
		console.log($task);
		if ($('.task-edit-btn', $task).prop('disabled')) return;
		$('.task-edit-btn', $task).prop('disabled', true);
		const editedTask = $('> .task-label input', $task).val();
		const taskId = $task.data('taskId');
		console.log("saving ", editedTask, taskId);
		$.ajax({
			url: '/tasks/' + taskId,
			type: 'PATCH',
			data: JSON.stringify({
				name: editedTask
			}),
			contentType: 'application/json'
		}).done((e) => {
			const $originalElement = $task.data('originalElement');
			$originalElement.data('taskId', taskId);
			$('> .task-label', $originalElement).text(editedTask);
			$('.task-edit-btn', $originalElement).click(() => {
				this.startEditingTask($originalElement);
			});

			$task.replaceWith($originalElement);
			this.registerHandlers();
		}).fail((e) => {
			console.error("Failed to update with error", e);
			$('.task-edit-btn', $task).prop('disabled', false);
		});
	}
	fetchProject() {
		$('.task-table > .task-entry:not(.Template):not(.task-entry-add)').remove();
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
		const $parentSub = $taskAdder.parent();
		if ($taskField[0].reportValidity && !$taskField[0].reportValidity()) return;
		$taskButton.prop('disabled', true);

		const newTask = $taskField.val().trim();
		const parentId = $parentSub.parent().data('taskId');
		const projectId = window.projectId;
		console.log(parentId, $taskAdder);
		$.post('/tasks', JSON.stringify({
			name: newTask,
			projectId: projectId,
			taskParent: parentId
		})).done((resp) => {
			console.log("Created new task", resp);
			$taskAdder.remove();
			this.renderTask(resp, $parentSub);
			this.registerHandlers();
			window.feather.replace();
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
		let pathArray = firstPath.split('.');
		pathArray = pathArray.slice(0, -2); // remove last two elements
		pathArray.push($knownTasks.length + 1);
		pathArray.push("");
		const finalPath = pathArray.join('.');
		$('.task-number', $thisToAdd).text(finalPath);

		$('.task-edit-btn', $thisToAdd).click(() => {
			this.addNewTask($thisToAdd);
		});
		$('.task-divide-num, .task-divide-btn', $thisToAdd).remove();
		$thisToAdd.insertBefore($addButton);
	}
	markTask($task) {
		const $completion = $('> .task-completion', $task);
		if ($completion.hasClass('disabled')) {
			return;
		}
		$completion.addClass('disabled', true);
		const isComplete = (!($('> .feather-check-square', $completion).length)).toString();
		const taskId = $task.data('taskId');

		$.post('/tasks/' + taskId + '/completed/' + isComplete).done((resp) => {
			console.log("Update marking", resp);
			if (isComplete == 'true') {
				$completion.html(`<span data-feather="check-square"></span>`);
				$task.addClass('task-done');
			} else {
				$completion.html(`<span data-feather="square"></span>`);
				$task.removeClass('task-done');
			}
			window.feather.replace();
			$completion.removeClass('disabled');
		}).fail((err) => {
			console.error("Failed to mark", err);
			$completion.removeClass('disabled');
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
