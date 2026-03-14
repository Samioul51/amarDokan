$(function(){

// User Register validation

	var $userRegister=$("#userRegister");

	$userRegister.validate({
		
		rules:{
			name:{
				required:true,
				lettersonly:true
			}
			,
			email: {
				required: true,
				space: true,
				email: true
			},
			mobileNumber: {
				required: true,
				space: true,
				numericOnly: true,
				minlength: 10,
				maxlength: 12

			},
			password: {
				required: true,
				space: true

			},
			confirmpassword: {
				required: true,
				space: true,
				equalTo: '#pass'

			},
			address: {
				required: true,
				all: true

			},

			city: {
				required: true,
				space: true

			},
			state: {
				required: true,


			},
			pincode: {
				required: true,
				space: true,
				numericOnly: true

			}, img: {
				required: true,
			}
			
		},
		messages:{
			name:{
				required:'name required',
				lettersonly:'invalid name'
			},
			email: {
				required: 'email name must be required',
				space: 'space not allowed',
				email: 'Invalid email'
			},
			mobileNumber: {
				required: 'mob no must be required',
				space: 'space not allowed',
				numericOnly: 'invalid mob no',
				minlength: 'min 10 digit',
				maxlength: 'max 12 digit'
			},

			password: {
				required: 'password must be required',
				space: 'space not allowed'

			},
			confirmpassword: {
				required: 'confirm password must be required',
				space: 'space not allowed',
				equalTo: 'password mismatch'

			},
			address: {
				required: 'address must be required',
				all: 'invalid'

			},

			city: {
				required: 'city must be required',
				space: 'space not allowed'

			},
			state: {
				required: 'state must be required',
				space: 'space not allowed'

			},
			pincode: {
				required: 'pincode must be required',
				space: 'space not allowed',
				numericOnly: 'invalid pincode'

			},
			img: {
				required: 'image required',
			}
		}
	})
	
	
// Orders Validation

var $orders=$("#orders");

$orders.validate({
		rules:{
			firstName:{
				required:true,
				lettersonly:true
			},
			lastName:{
				required:true,
				lettersonly:true
			}
			,
			email: {
				required: true,
				space: true,
				email: true
			},
			mobileNo: {
				required: true,
				space: true,
				numericOnly: true,
				minlength: 10,
				maxlength: 12

			},
			address: {
				required: true,
				all: true

			},

			city: {
				required: true,
				all: true
			},
			state: {
				required: true,
				all: true
			},
			pincode: {
				required: true,
				space: true,
				numericOnly: true
			},
			paymentType:{
			required: true
			}
		},
		messages:{
			firstName:{
				required:'first required',
				lettersonly:'invalid name'
			},
			lastName:{
				required:'last name required',
				lettersonly:'invalid name'
			},
			email: {
				required: 'email name must be required',
				space: 'space not allowed',
				email: 'Invalid email'
			},
			mobileNo: {
				required: 'mob no must be required',
				space: 'space not allowed',
				numericOnly: 'invalid mob no',
				minlength: 'min 10 digit',
				maxlength: 'max 12 digit'
			}
		   ,
			address: {
				required: 'address must be required',
				all: 'invalid'
			},

			city: {
				required: 'city must be required',
				all: 'invalid'
			},
			state: {
				required: 'state must be required',
				all: 'invalid'
			},
			pincode: {
				required: 'pincode must be required',
				space: 'space not allowed',
				numericOnly: 'invalid pincode'
			},
			paymentType:{
			required: 'select payment type'
			}
		}	
})

// Reset Password Validation

var $resetPassword=$("#resetPassword");

$resetPassword.validate({
		
		rules:{
			password: {
				required: true,
				space: true

			},
			confirmPassword: {
				required: true,
				space: true,
				equalTo: '#pass'

			}
		},
		messages:{
		   password: {
				required: 'password must be required',
				space: 'space not allowed'

			},
			confirmpassword: {
				required: 'confirm password must be required',
				space: 'space not allowed',
				equalTo: 'password mismatch'

			}
		}	
})
})


$(function () {
    const productForm = document.getElementById('productForm');

    if (productForm) {
        productForm.addEventListener('submit', async function (e) {
            e.preventDefault();

            const formData = new FormData(this);
            const msg = document.getElementById('msg');

            try {
                const response = await fetch('/api/products', {
                    method: 'POST',
                    body: formData
                });

                let message = 'Something went wrong on server';
                try {
                    const payload = await response.json();
                    if (payload && payload.message) {
                        message = payload.message;
                    }
                } catch (err) {
                }

                if (response.status === 201) {
                    msg.className = 'text-success fw-bold';
                    msg.innerText = 'Saved successfully';
                    setTimeout(() => location.reload(), 1000);
                } else {
                    msg.className = 'text-danger fw-bold';
                    msg.innerText = message;
                }
            } catch (error) {
                msg.className = 'text-danger fw-bold';
                msg.innerText = 'Error: ' + error.message;
            }
        });
    }
});

$(function () {
    const categoryForm = document.getElementById('categoryForm');

    if (categoryForm) {
        categoryForm.addEventListener('submit', async function (e) {
            e.preventDefault();

            const formData = new FormData(this);
            const msg = document.getElementById('msg');

            try {
                const response = await fetch('/api/categories', {
                    method: 'POST',
                    body: formData
                });

                let message = 'Something went wrong on server';
                try {
                    const payload = await response.json();
                    if (payload && payload.message) {
                        message = payload.message;
                    }
                } catch (err) {
                }

                if (response.status === 201) {
                    msg.className = 'text-success fw-bold';
                    msg.innerText = 'Saved successfully';
                    setTimeout(() => location.reload(), 1000);
                } else if (response.status === 409) {
                    msg.className = 'text-danger fw-bold';
                    msg.innerText = 'Category name already exists';
                } else {
                    msg.className = 'text-danger fw-bold';
                    msg.innerText = message;
                }
            } catch (error) {
                msg.className = 'text-danger fw-bold';
                msg.innerText = 'Error: ' + error.message;
            }
        });
    }
});

window.updateStatus = async function (id, status) {
    try {
        const response = await fetch(`/api/users/${id}/status?status=${status}`, {
            method: 'PUT'
        });

        let message = 'Failed to update status';
        try {
            const payload = await response.json();
            if (payload && payload.message) {
                message = payload.message;
            }
        } catch (err) {
        }

        if (response.ok) {
            location.reload();
        } else {
            alert(message);
        }
    } catch (error) {
        alert('Error: ' + error.message);
    }
};

window.deleteCategory = async function (id) {
    if (!confirm('Are you sure you want to delete this category?')) {
        return;
    }

    try {
        const response = await fetch('/api/categories/' + id, {
            method: 'DELETE'
        });

        let message = 'Failed to delete category';
        try {
            const payload = await response.json();
            if (payload && payload.message) {
                message = payload.message;
            }
        } catch (err) {
        }

        if (response.ok) {
            location.reload();
        } else {
            alert(message);
        }
    } catch (error) {
        alert('Error: ' + error.message);
    }
};

window.deleteProduct = async function (id) {
    if (!confirm('Are you sure you want to delete this product?')) {
        return;
    }

    try {
        const response = await fetch('/api/products/' + id, {
            method: 'DELETE'
        });

        let message = 'Failed to delete product';
        try {
            const payload = await response.json();
            if (payload && payload.message) {
                message = payload.message;
            }
        } catch (err) {
        }

        if (response.ok) {
            location.reload();
        } else {
            alert(message);
        }
    } catch (error) {
        alert('Error: ' + error.message);
    }
};



jQuery.validator.addMethod('lettersonly', function(value, element) {
		return /^[^-\s][a-zA-Z_\s-]+$/.test(value);
	});
	
		jQuery.validator.addMethod('space', function(value, element) {
		return /^[^-\s]+$/.test(value);
	});

	jQuery.validator.addMethod('all', function(value, element) {
		return /^[^-\s][a-zA-Z0-9_,.\s-]+$/.test(value);
	});


	jQuery.validator.addMethod('numericOnly', function(value, element) {
		return /^[0-9]+$/.test(value);
	});
