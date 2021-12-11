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
		$('.task-table > .task-entry:not(.Template):not(.task-entry-add)').remove();
		$('#projectTeamList li:not(.addNewMember').remove();
		$('.assign-task-float .task-assignments').children().remove();
		this.$navProjTitle.text(project.title);
		this.$projectTitle.text(project.title);
		$('#projectView > .container').addClass('animate__animated animate__fadeIn');
		if (project.archived) {
			this.$navProjTitle.text("[Archived] " + project.title);
			$('body').addClass('project-archived');
		}
		if (project.teammates) {
			for (const teammate of project.teammates) this.renderTeammate(teammate);
		}
		if (project.tasks) {
			for (const task of project.tasks) this.renderTask(task, null);
		}
	}
	truncateName(name) {
		const splitted = name.split(' ');
		let resultName = "";
		resultName += splitted[0].charAt(0).toUpperCase();
		if (splitted.length > 1)
			resultName += splitted[1].charAt(0).toUpperCase();
		return resultName;
	}
	renderTeammate(teammate) {
		$(document.createElement("li"))
			.text(teammate.name)
			.insertBefore($('#projectTeamList .addNewMember'));
		$(document.createElement("button"))
			.text(this.truncateName(teammate.name))
			.attr('title', teammate.name)
			.addClass("btn task-assignment")
			.attr('data-tmid', teammate.id)
			.appendTo('.assign-task-float .task-assignments');
	}
	renderTask(task, parent) {
		const $addTo = (parent) ? parent : this.$projectTaskList;
		const $thisTask = this.$projectTaskTemplate.clone();
		$thisTask.insertBefore($('> .task-entry-add', $addTo));
		$thisTask.removeAttr('id').removeClass('Template').attr('data-taskId', task.id);
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
			$('> .task-assignments .task-assignment-box', $thisTask).remove();
			$thisTask.addClass('task-withSubs');
			const $subtaskBox = $('> .task-subtasks', $thisTask);
			for (const subtask of task.subtasks) {
				this.renderTask(subtask, $subtaskBox);
			}
		} else {
			$('> .task-subtasks', $thisTask).remove();
			if (task.assignedTeammates.length) {
				for (const teammate of task.assignedTeammates) {
					const assignment = $(`.assign-task-float .task-assignment[data-tmid="${teammate.id}"]`).clone();
					assignment.prependTo($('> .task-assignments .task-assignment-box', $thisTask));
				}
			}
		}
		$thisTask.addClass('task-ctrl-default-state');
	}
	startEditingTask($task) {
		console.log($task);
		$task.closest('.task-table > .task-entry').addClass('task-ctrl-editing-nested');

		const taskValue = $('> .task-label', $task).text();
		const taskNumber = $('> .task-number', $task).text();

		const $thisToAdd = this.$addTaskTemplate.clone();
		$thisToAdd.removeAttr('id').removeClass('Template').attr('data-taskId', $task.attr('data-taskId'));
		$('> .task-number', $thisToAdd).text(taskNumber);
		$('> .task-label input', $thisToAdd).val(taskValue);
		if ($('> .task-subtasks', $task).children().length) {
			//has subclasses
			$('.task-divide-num, .task-divide-btn', $thisToAdd).remove();
			const $editingSubs = $('> .task-subtasks', $task).clone();
			$('.task-edit, .task-assignments', $editingSubs).remove();
			$('.task-entry-add', $editingSubs).remove();
			$editingSubs.appendTo($thisToAdd);
		}
		$thisToAdd.addClass('task-ctrl-edit-existing');
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
				$newSub.addClass('task-ctrl-save-subtasks');
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
			const baseTask = $task.attr('data-taskId');
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
		if ($('> .task-edit .task-edit-btn', $task).prop('disabled')) return;
		$('> .task-edit .task-edit-btn', $task).prop('disabled', true);
		const editedTask = $('> .task-label input', $task).val();
		const taskId = $task.attr('data-taskId');
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
			$originalElement.attr('data-taskId', taskId);
			$('> .task-label', $originalElement).text(editedTask);
			$task.replaceWith($originalElement);
			$originalElement.closest('.task-table > .task-entry').removeClass('task-ctrl-editing-nested');
		}).fail((e) => {
			console.error("Failed to update with error", e);
			$('> .task-edit .task-edit-btn', $task).prop('disabled', false);
		});
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
		const $parentSub = $taskAdder.parent();
		if ($taskField[0].reportValidity && !$taskField[0].reportValidity()) return;
		$taskButton.prop('disabled', true);

		const newTask = $taskField.val().trim();
		const parentId = $parentSub.parent().attr('data-taskId');
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
			$parentSub.closest('.task-table > .task-entry').removeClass('task-ctrl-editing-nested');
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
		$('> .task-number', $thisToAdd).text(finalPath);
		$thisToAdd.addClass('task-ctrl-edit-new');
		$('> .task-edit .task-divide-num, > .task-edit .task-divide-btn', $thisToAdd).remove();
		$thisToAdd.insertBefore($addButton);
		$thisToAdd.closest('.task-table > .task-entry').addClass('task-ctrl-editing-nested');

	}
	markTask($task) {
		const $completion = $('> .task-completion', $task);
		if ($completion.hasClass('disabled')) {
			return;
		}
		$completion.addClass('disabled', true);
		const isComplete = (!($('> .feather-check-square', $completion).length)).toString();
		const taskId = $task.attr('data-taskId');

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
	presentAssign($task) {
		const $taskFloat = $('.assign-task-float');
		if (!$taskFloat.hasClass('d-none')) {
			if ($taskFloat.attr('data-floatTaskId') == $task.attr('data-taskId'))
				return this.hideAssign();
		}
		//get position
		const addBtn = $('> .task-assignments .btn.task-assignment.task-assign', $task);
		const topPos = addBtn.offset().top + addBtn.outerHeight(true) + 7.07;
		$taskFloat.removeClass('d-none');
		const leftPos = addBtn.offset().left - $taskFloat.width() + 17.07;
		$taskFloat.css({
			top: topPos + "px",
			left: leftPos + "px"
		});
		$task.addClass('task-ctrl-assigning');
		$taskFloat.attr('data-floatTaskId', $task.attr('data-taskId'));
		$('.assign-task-float .task-assignment').prop('disabled', false);
		for (const existingAssn of $('> .task-assignments .task-assignment', $task).toArray()) {
			$(`.assign-task-float .task-assignment[data-tmid="${$(existingAssn).attr('data-tmid')}"]`)
				.prop('disabled', true);
		}
	}
	hideAssign() {
		const $taskFloat = $('.assign-task-float');
		$taskFloat.addClass('d-none').removeAttr('data-floatTaskId');
	}
	assignCurrentTask(assignment) {
		const assignTid = $(assignment).attr('data-tmid');
		const targetTask = $('.assign-task-float').attr('data-floatTaskId');
		const $assignTask = $(`.task-entry[data-taskId="${targetTask}"]`);
		$(assignment).prop('disabled', true);
		$.post({
			url: '/tasks/' + targetTask + '/assignments',
			data: JSON.stringify({
				teammateId: assignTid
			}),
			contentType: 'application/json'
		}).done((e) => {
			const assignment = $(`.assign-task-float .task-assignment[data-tmid="${assignTid}"]`).clone();
			assignment.prop('disabled', false);
			assignment.prependTo($('> .task-assignments .task-assignment-box', $assignTask));
			this.hideAssign();
		}).fail((e) => {
			console.log(e);
			$(assignment).prop('disabled', false);
		});
	}

	unassignTask(task, assn) {
		if ($(assn).prop('disabled')) return;
		$(assn).prop('disabled', true);
		this.hideAssign();
		const teammateId = $(assn).attr('data-tmid');
		const taskId = $(task).attr('data-taskId');
		$.post({
			url: '/tasks/' + taskId + '/assignments/' + teammateId + '/unassign',
		}).done((e) => {
			$(assn).remove();
		}).fail((e) => {
			console.error(e);
			$(assn).prop('disabled', true);
		});
	}

	registerHandlers() {
		$(document).off('click');

		$(document).on('click', '.task-entry-add', (e) => {
			const $addTo = $(e.currentTarget);
			this.setupTaskAdder($addTo);
		});
		$(document).on('click', '.task-ctrl-default-state .task-completion', (e) => {
			const $taskToMark = $(e.currentTarget).closest('.task-entry');
			this.markTask($taskToMark)
		});
		$(document).on('click', '.task-edit .task-edit-btn', (e) => {
			const $task = $(e.currentTarget).closest('.task-entry');
			if ($task.hasClass('task-ctrl-edit-new')) {
				this.addNewTask($task);
			} else if ($task.hasClass('task-ctrl-edit-existing')) {
				this.saveEditedTask($task);
			} else if ($task.hasClass('task-ctrl-save-subtasks')) {
				this.saveDividedTask($task.parent().closest('.task-entry'));
			} else if ($task.hasClass('task-ctrl-default-state')) {
				this.startEditingTask($task);
			}
		});
		$(document).on('click', '.task-entry .task-assignments .btn.task-assignment.task-assign', (e) => {
			const $task = $(e.currentTarget).closest('.task-entry');
			this.presentAssign($task);
		});
		$(document).on('click', '.task-entry .task-assignments .btn.task-assignment:not(.task-assign)', (e) => {
			const $task = $(e.currentTarget).closest('.task-entry');
			this.unassignTask($task, e.currentTarget);
		});
		$(document).on('click', '.assign-task-float .task-assignments .btn', (e) => {
			this.assignCurrentTask(e.currentTarget);
		})
		$(document).on('click', '.task-edit .task-divide-btn', (e) => {
			const $task = $(e.currentTarget).closest('.task-entry');
			this.divideTask($task);
		})
	}
}

export {
	ProjectView
};
